    public static void createPortalLogin(String login, String password, String name) throws Exception {
        String sql = "";
        Connection conn = null;
        Db db = null;
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
                sql = "select user_login from users where user_login = '" + login + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true;
            }
            if (!found) {
                {
                    r.add("user_login", login);
                    r.add("user_password", lebah.util.PasswordService.encrypt(password));
                    r.add("user_name", name);
                    r.add("user_role", "student");
                    sql = r.getSQLInsert("users");
                    stmt.executeUpdate(sql);
                }
                {
                    String css_name = "default.css";
                    sql = "insert into user_css (user_login, css_name) values ('" + login + "', '" + css_name + "')";
                    stmt.executeUpdate(sql);
                }
                {
                    Vector vector = new Vector();
                    {
                        sql = "select tab_id, tab_title, sequence, display_type from tab_template where user_login = 'student'";
                        ResultSet rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            Hashtable h = new Hashtable();
                            h.put("tab_id", rs.getString("tab_id"));
                            h.put("tab_title", rs.getString("tab_title"));
                            h.put("sequence", rs.getString("sequence"));
                            h.put("display_type", rs.getString("display_type"));
                            vector.addElement(h);
                        }
                    }
                    {
                        for (int i = 0; i < vector.size(); i++) {
                            Hashtable h = (Hashtable) vector.elementAt(i);
                            r.clear();
                            r.add("tab_id", (String) h.get("tab_id"));
                            r.add("tab_title", (String) h.get("tab_title"));
                            r.add("sequence", Integer.parseInt((String) h.get("sequence")));
                            r.add("display_type", (String) h.get("display_type"));
                            r.add("user_login", login);
                            sql = r.getSQLInsert("tabs");
                            stmt.executeUpdate(sql);
                        }
                    }
                }
                {
                    Vector vector = new Vector();
                    {
                        sql = "select tab_id, module_id, sequence, module_custom_title, column_number " + "from user_module_template where user_login = 'student'";
                        ResultSet rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            Hashtable h = new Hashtable();
                            h.put("tab_id", rs.getString("tab_id"));
                            h.put("module_id", rs.getString("module_id"));
                            h.put("sequence", rs.getString("sequence"));
                            h.put("module_custom_title", lebah.db.Db.getString(rs, "module_custom_title"));
                            String coln = lebah.db.Db.getString(rs, "column_number");
                            h.put("column_number", coln.equals("") ? "0" : coln);
                            vector.addElement(h);
                        }
                    }
                    if (vector.size() > 0) {
                        for (int i = 0; i < vector.size(); i++) {
                            Hashtable h = (Hashtable) vector.elementAt(i);
                            r.clear();
                            r.add("tab_id", (String) h.get("tab_id"));
                            r.add("module_id", (String) h.get("module_id"));
                            r.add("sequence", Integer.parseInt((String) h.get("sequence")));
                            r.add("module_custom_title", (String) h.get("module_custom_title"));
                            r.add("column_number", Integer.parseInt((String) h.get("column_number")));
                            r.add("user_login", login);
                            sql = r.getSQLInsert("user_module");
                            stmt.executeUpdate(sql);
                        }
                    }
                }
            } else {
                r.add("user_name", name);
                r.update("user_login", login);
                sql = r.getSQLUpdate("users");
                stmt.executeUpdate(sql);
            }
            conn.commit();
        } catch (Exception ex) {
            try {
                conn.rollback();
            } catch (SQLException rollex) {
            }
            throw ex;
        } finally {
            if (db != null) db.close();
        }
    }
