    public static void assignRoles(String module_id, String[] roles) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            Statement stmt = db.getStatement();
            conn.setAutoCommit(false);
            sql = "DELETE FROM role_module WHERE module_id = '" + module_id + "'";
            stmt.executeUpdate(sql);
            for (int i = 0; i < roles.length; i++) {
                sql = "INSERT INTO role_module (module_id, user_role) VALUES ('" + module_id + "', '" + roles[i] + "')";
                stmt.executeUpdate(sql);
            }
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
