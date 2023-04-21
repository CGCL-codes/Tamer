    public void testSetReadOnly() throws Exception {
        boolean expResult = true;
        Connection conn = newConnection();
        try {
            conn.setReadOnly(expResult);
        } catch (SQLException ex) {
            fail(ex.toString());
        }
        boolean result = conn.isReadOnly();
        assertEquals(expResult, result);
        expResult = false;
        try {
            conn.setReadOnly(expResult);
        } catch (SQLException ex) {
            fail(ex.toString());
        }
        result = conn.isReadOnly();
        assertEquals(expResult, result);
        super.executeScript("setup-dual-table.sql");
        conn.setReadOnly(true);
        Statement stmt = connectionFactory().createStatement(conn);
        try {
            stmt.executeUpdate("insert into dual values 'read-only'");
            fail("Allowed write while readonly");
        } catch (Exception e) {
        }
        conn.setReadOnly(false);
        try {
            stmt.executeUpdate("insert into dual values 'read-write'");
        } catch (Exception e) {
            fail("Insert failed while read-write");
        }
    }
