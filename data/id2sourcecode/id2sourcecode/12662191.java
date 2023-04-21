    @Test
    public void testRollback() throws Exception {
        InitialContext ic = new InitialContext();
        TransactionManager tm = (TransactionManager) ic.lookup("TransactionManager");
        DataSource ds = (DataSource) ic.lookup("TestDS");
        assertNotNull(ds);
        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;
        tm.begin();
        try {
            conn = (Connection) ds.getConnection();
            executeUpdate(conn, "insert into test(id) values (1)");
        } finally {
            close(rs);
            close(s);
            close(conn);
            tm.rollback();
        }
        tm.begin();
        try {
            conn = (Connection) ds.getConnection();
            s = conn.createStatement();
            rs = s.executeQuery("select id from test");
            assertFalse(rs.next());
        } finally {
            close(rs);
            close(s);
            close(conn);
            tm.rollback();
        }
    }
