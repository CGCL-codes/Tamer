    public static boolean add(String fullname, String username, String password, String role, String style) throws Exception {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            Statement stmt = db.getStatement();
            conn.setAutoCommit(false);
            SQLRenderer r = new SQLRenderer();
            {
                r.add("user_login");
                r.add("user_login", username);
                sql = r.getSQLSelect("users");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next() && (username.equalsIgnoreCase(rs.getString("user_login")))) return false;
            }
            {
                r.add("user_login_alt");
                r.add("user_login_alt", username);
                sql = r.getSQLSelect("users");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next() && (username.equalsIgnoreCase(rs.getString("user_login_alt")))) return false;
            }
            {
                r.clear();
                r.add("user_login", username);
                r.add("user_password", lebah.util.PasswordService.encrypt(password));
                r.add("user_name", fullname);
                r.add("user_role", role);
                r.add("date_registered", lebah.util.DateTool.getCurrentDatetime());
                sql = r.getSQLInsert("users");
                stmt.executeUpdate(sql);
                setPageStyle(username, style);
            }
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex2) {
            }
            Log.print(ex.getMessage() + "\n" + sql);
            return false;
        } finally {
            if (db != null) db.close();
        }
        return true;
    }
