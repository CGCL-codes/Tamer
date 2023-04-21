    public static boolean add(String module_id, String module_title, String module_class, String module_group, String module_description, String[] roles) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            Statement stmt = db.getStatement();
            conn.setAutoCommit(false);
            SQLRenderer r = new SQLRenderer();
            r.add("module_id");
            r.add("module_id", module_id);
            sql = r.getSQLSelect("module");
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next() && (module_id.equalsIgnoreCase(rs.getString("module_id")))) return false;
            r = new SQLRenderer();
            r.add("module_id", module_id);
            r.add("module_title", module_title);
            r.add("module_class", module_class);
            r.add("module_group", module_group.toUpperCase());
            r.add("module_description", module_description);
            sql = r.getSQLInsert("module");
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
