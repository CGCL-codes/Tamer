    public boolean populate() {
        boolean bRet = false;
        if (DbUtil.isDbConfigured()) return true;
        DataSource dataSource = DataSourceManager.getDataSource();
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            conn.setAutoCommit(false);
            ((SmartDataSourceEx) dataSource).setConnectionClose(true);
            SqlReader in = null;
            String filename = "";
            if (AppState.isLibMode()) {
                if (AppState.isDemoDerby()) filename = "derby_10.1.3.1.sql"; else if (AppState.isDemoHsqldb()) filename = "hsqldb_1.7.3.3.sql"; else filename = "h2_1.0.sql";
            } else {
                if (AppState.isDemoDerby()) filename = "sql.1.2.complete/derby_10.1.3.1.sql"; else if (AppState.isDemoHsqldb()) filename = "sql.1.2.complete/hsqldb_1.7.3.3.sql"; else filename = "sql.1.2.complete/h2_1.0.sql";
            }
            java.net.URL url = FileUtil.toURL(filename);
            log.info(filename + " --> url: " + url);
            in = new SqlReader(new InputStreamReader(url.openStream()));
            String sql;
            int rowAffected;
            SqlUpdate sqlUpdate = null;
            while (true) {
                sql = in.nextSql();
                if ((sql == null) || (sql.length() == 0)) break;
                sqlUpdate = new SqlUpdate(dataSource, sql);
                rowAffected = sqlUpdate.update();
                if (AppState.isVerbose()) System.out.println("execute - " + sql);
                log.info("execute - " + sql);
            }
            sqlUpdate = null;
            conn.commit();
            ((SmartDataSourceEx) dataSource).setConnectionClose(true);
            in.close();
            bRet = true;
        } catch (Exception e) {
            System.out.println(e);
            log.error(e, e);
            try {
                log.info("conn.rollback()");
                conn.rollback();
            } catch (Exception e1) {
            }
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return bRet;
    }
