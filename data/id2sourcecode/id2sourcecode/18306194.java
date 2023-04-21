    public static void delete(String usrlogin) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            sql = "DELETE FROM user_module WHERE user_login = '" + usrlogin + "'";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM users WHERE user_login = '" + usrlogin + "'";
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
