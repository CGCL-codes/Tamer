    public void testTransaction() throws SQLException {
        try {
            this.conn.setAutoCommit(false);
            this.stmt.executeUpdate("INSERT INTO trans_test (id, decdata) VALUES (1, 1.0)");
            this.conn.rollback();
            this.rs = this.stmt.executeQuery("SELECT * from trans_test");
            boolean hasResults = this.rs.next();
            assertTrue("Results returned, rollback to empty table failed", (hasResults != true));
            this.stmt.executeUpdate("INSERT INTO trans_test (id, decdata) VALUES (2, " + DOUBLE_CONST + ")");
            this.conn.commit();
            this.rs = this.stmt.executeQuery("SELECT * from trans_test where id=2");
            hasResults = this.rs.next();
            assertTrue("No rows in table after INSERT", hasResults);
            double doubleVal = this.rs.getDouble(2);
            double delta = Math.abs(DOUBLE_CONST - doubleVal);
            assertTrue("Double value returned != " + DOUBLE_CONST, (delta < EPSILON));
        } finally {
            this.conn.setAutoCommit(true);
        }
    }
