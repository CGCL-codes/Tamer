    public static boolean update(String module_id, String module_title, String module_class, String module_group, String module_description, String[] roles) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            conn.setAutoCommit(false);
            r.add("module_title", module_title);
            r.add("module_class", module_class);
            r.add("module_group", module_group.toUpperCase());
            r.add("module_description", module_description);
            r.update("module_id", module_id);
            sql = r.getSQLUpdate("module");
            stmt.executeUpdate(sql);
            if (roles != null) {
                sql = "DELETE FROM role_module WHERE module_id = '" + module_id + "'";
                stmt.executeUpdate(sql);
                for (int i = 0; i < roles.length; i++) {
                    sql = "INSERT INTO role_module (module_id, user_role) VALUES ('" + module_id + "', '" + roles[i] + "')";
                    stmt.executeUpdate(sql);
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex2) {
            }
            return false;
        } finally {
            if (db != null) db.close();
        }
        return true;
    }
