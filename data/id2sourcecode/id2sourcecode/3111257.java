    public static void fixTabSequence(String usrlogin) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            boolean fix = false;
            {
                sql = "SELECT sequence FROM tabs WHERE user_login = '" + usrlogin + "' AND sequence = 0";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) fix = true;
            }
            if (fix) {
                Vector v = new Vector();
                sql = "SELECT tab_id FROM tabs WHERE user_login = '" + usrlogin + "'";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String tab_id = rs.getString("tab_id");
                    v.addElement(tab_id);
                }
                for (int i = 0; i < v.size(); i++) {
                    String tab_id = (String) v.elementAt(i);
                    sql = "UPDATE tabs SET sequence = " + Integer.toString(i + 1) + " WHERE tab_id = '" + tab_id + "' AND user_login = '" + usrlogin + "'";
                    Log.print(sql);
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
