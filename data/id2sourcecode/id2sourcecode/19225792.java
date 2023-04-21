    private int createColumn(Properties ctx, MTable table, MColumn column, boolean doAlter) {
        int no = 0;
        String sql = null;
        ResultSet rst = null;
        ResultSet rsc = null;
        Connection conn = null;
        Trx trx = Trx.get(getTrxName(ctx), true);
        if (!trx.commit()) return 0;
        try {
            conn = trx.getConnection();
            DatabaseMetaData md = conn.getMetaData();
            String catalog = DB.getDatabase().getCatalog();
            String schema = DB.getDatabase().getSchema();
            String tableName = table.getTableName();
            String columnName = column.getColumnName();
            if (DB.isOracle()) {
                tableName = tableName.toUpperCase();
                columnName = columnName.toUpperCase();
            } else if (DB.isPostgreSQL()) {
                tableName = tableName.toLowerCase();
                columnName = columnName.toLowerCase();
            }
            rst = md.getTables(catalog, schema, tableName, new String[] { "TABLE" });
            if (!rst.next()) {
                sql = table.getSQLCreate();
            } else {
                rsc = md.getColumns(catalog, schema, tableName, columnName);
                if (rsc.next()) {
                    if (doAlter) {
                        boolean notNull = DatabaseMetaData.columnNoNulls == rsc.getInt("NULLABLE");
                        sql = column.getSQLModify(table, column.isMandatory() != notNull);
                    }
                } else {
                    sql = column.getSQLAdd(table);
                }
                rsc.close();
                rsc = null;
            }
            rst.close();
            rst = null;
            if (sql != null && sql.trim().length() > 0) {
                log.info(sql);
                if (sql.indexOf(DB.SQLSTATEMENT_SEPARATOR) == -1) {
                    no = DB.executeUpdate(sql, false, trx.getTrxName());
                    if (no == -1) return 0;
                } else {
                    String statements[] = sql.split(DB.SQLSTATEMENT_SEPARATOR);
                    for (int i = 0; i < statements.length; i++) {
                        int count = DB.executeUpdate(statements[i], false, trx.getTrxName());
                        if (count == -1) {
                            return 0;
                        }
                        no += count;
                    }
                }
            }
            trx.commit(true);
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            if (rsc != null) {
                try {
                    rsc.close();
                } catch (SQLException e1) {
                }
                rsc = null;
            }
            if (rst != null) {
                try {
                    rst.close();
                } catch (SQLException e1) {
                }
                rst = null;
            }
            trx.rollback();
            return 0;
        }
        return 1;
    }
