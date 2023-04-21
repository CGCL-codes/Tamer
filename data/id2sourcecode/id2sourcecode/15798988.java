    public void testQueryTimeout() throws Exception {
        try {
            dropTable("jtdsStmtTest");
            Statement stmt = con.createStatement();
            stmt.execute("CREATE TABLE jtdsStmtTest (id int primary key, data text)");
            assertEquals(1, stmt.executeUpdate("INSERT INTO jtdsStmtTest VALUES(1, " + "'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')"));
            assertEquals(1, stmt.executeUpdate("INSERT INTO jtdsStmtTest VALUES(2, " + "'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')"));
            try {
                stmt.setQueryTimeout(-1);
                fail("Expected error timeout < 0");
            } catch (SQLException e) {
                assertEquals("HY092", e.getSQLState());
            }
            con.setAutoCommit(false);
            assertEquals(1, stmt.executeUpdate("UPDATE jtdsStmtTest SET data = '' WHERE id = 1"));
            Connection con2 = getConnection();
            Statement stmt2 = con2.createStatement();
            stmt2.setQueryTimeout(1);
            assertEquals(1, stmt2.getQueryTimeout());
            try {
                stmt2.executeQuery("SELECT * FROM jtdsStmtTest WHERE id = 1");
                fail("Expected time out exception");
            } catch (SQLException e) {
                assertEquals("HYT00", e.getSQLState());
            }
            try {
                stmt2.close();
            } catch (SQLException e) {
                fail("Not expecting a cancel ACK exception.");
            }
            con2.close();
            con.rollback();
            stmt.close();
        } finally {
            con.setAutoCommit(true);
            dropTable("jtdsStmtTest");
        }
    }
