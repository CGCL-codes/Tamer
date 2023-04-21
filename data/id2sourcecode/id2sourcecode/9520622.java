    public int executeUpdate(String sql) {
        Connection conn = null;
        Statement stmt = null;
        int rows = 0;
        Transaction tran = null;
        try {
            Session session = SessionFactory.getSession();
            tran = session.beginTransaction();
            conn = session.connection();
            stmt = conn.createStatement();
            stmt.setEscapeProcessing(false);
            rows = stmt.executeUpdate(sql);
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
        return rows;
    }
