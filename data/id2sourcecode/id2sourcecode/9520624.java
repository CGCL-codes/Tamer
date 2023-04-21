    public int insert(String sql) {
        int id = 0;
        Connection conn = null;
        Statement stmt = null;
        Transaction tran = null;
        try {
            Session session = SessionFactory.getSession();
            tran = session.beginTransaction();
            conn = session.connection();
            stmt = conn.createStatement();
            stmt.setEscapeProcessing(false);
            int rows = stmt.executeUpdate(sql);
            if (rows > 0) {
                ResultSet rs = stmt.executeQuery("SELECT last_insert_id()");
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                rs.close();
                rs = null;
            }
            tran.commit();
        } catch (SQLException e) {
            tran.rollback();
            e.printStackTrace();
        } finally {
            try {
                if (tran != null) {
                    tran = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }
