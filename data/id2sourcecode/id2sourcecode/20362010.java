    private static void setPageStyle(String usrlogin, String style) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            sql = "delete from user_css where user_login = '" + usrlogin + "'";
            stmt.executeUpdate(sql);
            SQLRenderer r = new SQLRenderer();
            r.add("user_login", usrlogin);
            r.add("css_name", style);
            sql = r.getSQLInsert("user_css");
            stmt.executeUpdate(sql);
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException exr) {
            }
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }
