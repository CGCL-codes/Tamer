    public static void delete(String module_id) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            sql = "DELETE FROM role_module WHERE module_id = '" + module_id + "'";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM user_module WHERE module_id = '" + module_id + "'";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM role_module WHERE module_id = '" + module_id + "'";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM module WHERE module_id = '" + module_id + "'";
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
