    public static void setUserModule(String usrlogin, String role) throws Exception {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            sql = "DELETE FROM tabs WHERE user_login = '" + usrlogin + "'";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM user_module WHERE user_login = '" + usrlogin + "'";
            stmt.executeUpdate(sql);
            SQLRenderer r = new SQLRenderer();
            {
                r.add("tab_id");
                r.add("tab_title");
                r.add("sequence");
                r.add("display_type");
                r.add("user_login", role);
                sql = r.getSQLSelect("tab_template");
                ResultSet rs = stmt.executeQuery(sql);
                Vector tabs = new Vector();
                while (rs.next()) {
                    Hashtable h = new Hashtable();
                    h.put("tab_id", rs.getString("tab_id"));
                    h.put("tab_title", rs.getString("tab_title"));
                    h.put("sequence", new Integer(rs.getInt("sequence")));
                    h.put("display_type", rs.getString("display_type"));
                    h.put("user_login", usrlogin);
                    tabs.addElement(h);
                }
                for (int i = 0; i < tabs.size(); i++) {
                    Hashtable h = (Hashtable) tabs.elementAt(i);
                    r.clear();
                    r.add("tab_id", (String) h.get("tab_id"));
                    r.add("tab_title", (String) h.get("tab_title"));
                    r.add("sequence", ((Integer) h.get("sequence")).intValue());
                    r.add("display_type", (String) h.get("display_type"));
                    r.add("user_login", usrlogin);
                    sql = r.getSQLInsert("tabs");
                    stmt.executeUpdate(sql);
                }
            }
            {
                r.clear();
                r.add("tab_id");
                r.add("module_id");
                r.add("sequence");
                r.add("module_custom_title");
                r.add("column_number");
                r.add("user_login", role);
                sql = r.getSQLSelect("user_module_template");
                ResultSet rs = stmt.executeQuery(sql);
                Vector modules = new Vector();
                while (rs.next()) {
                    int sequence = 1;
                    String seq = rs.getString("sequence") != null ? rs.getString("sequence") : "1";
                    if (seq != null && !"".equals(seq)) sequence = Integer.parseInt(seq);
                    int colnum = 1;
                    String col = rs.getString("column_number") != null ? rs.getString("column_number") : "0";
                    if (col != null && !"".equals(col)) colnum = Integer.parseInt(col);
                    Hashtable h = new Hashtable();
                    h.put("tab_id", rs.getString("tab_id"));
                    h.put("module_id", rs.getString("module_id"));
                    h.put("sequence", new Integer(sequence));
                    h.put("module_custom_title", Db.getString(rs, "module_custom_title"));
                    h.put("column_number", new Integer(colnum));
                    h.put("user_login", usrlogin);
                    modules.addElement(h);
                }
                for (int i = 0; i < modules.size(); i++) {
                    Hashtable h = (Hashtable) modules.elementAt(i);
                    r.clear();
                    r.add("tab_id", (String) h.get("tab_id"));
                    r.add("module_id", (String) h.get("module_id"));
                    r.add("sequence", ((Integer) h.get("sequence")).intValue());
                    r.add("module_custom_title", (String) h.get("module_custom_title"));
                    r.add("column_number", ((Integer) h.get("column_number")).intValue());
                    r.add("user_login", usrlogin);
                    sql = r.getSQLInsert("user_module");
                    stmt.executeUpdate(sql);
                }
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
