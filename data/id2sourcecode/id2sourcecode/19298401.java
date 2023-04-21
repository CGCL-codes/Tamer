    public static void updateAttributeData(String module_id, String[] attributes, String[] values) throws DbException {
        if (attributes == null) {
            System.out.println("Error: attributes is null");
            return;
        }
        if (values == null) {
            System.out.println("Error: values is null");
            return;
        }
        Db db = null;
        String sql = "";
        Connection conn = null;
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            SQLRenderer r = new SQLRenderer();
            sql = "DELETE FROM attr_module_data WHERE module_id = '" + module_id + "'";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO attr_module_data ";
            for (int i = 0; i < attributes.length; i++) {
                r.clear();
                r.add("module_id", module_id);
                r.add("attribute_name", attributes[i]);
                r.add("attribute_value", values[i]);
                sql = r.getSQLInsert("attr_module_data");
                stmt.executeUpdate(sql);
            }
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException rex) {
            }
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if (db != null) db.close();
        }
    }
