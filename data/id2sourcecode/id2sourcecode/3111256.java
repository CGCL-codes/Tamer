    public static void changeSequence(String usrlogin, String tab, String pos) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            int sequence = 0;
            {
                sql = "SELECT sequence FROM tabs WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    sequence = rs.getInt("sequence");
                }
            }
            String tab2 = "";
            if ("down".equals(pos)) {
                sql = "SELECT tab_id FROM tabs WHERE user_login = '" + usrlogin + "' AND sequence = " + Integer.toString(++sequence);
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) tab2 = rs.getString("tab_id");
                if (!"".equals(tab2)) {
                    sql = "UPDATE tabs SET sequence = " + sequence + " WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                    stmt.executeUpdate(sql);
                    sql = "UPDATE tabs SET sequence = " + Integer.toString(--sequence) + " WHERE tab_id = '" + tab2 + "' AND user_login = '" + usrlogin + "'";
                    stmt.executeUpdate(sql);
                }
            } else if ("up".equals(pos)) {
                sql = "SELECT tab_id FROM tabs WHERE user_login = '" + usrlogin + "' AND sequence = " + Integer.toString(--sequence);
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) tab2 = rs.getString("tab_id");
                if (!"".equals(tab2)) {
                    sql = "UPDATE tabs SET sequence = " + sequence + " WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                    stmt.executeUpdate(sql);
                    sql = "UPDATE tabs SET sequence = " + Integer.toString(++sequence) + " WHERE tab_id = '" + tab2 + "' AND user_login = '" + usrlogin + "'";
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
