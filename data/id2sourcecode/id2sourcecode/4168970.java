    public void testRollBackWithoutError() throws Exception {
        conn.setAutoCommit(false);
        try {
            executeUpdate("insert into pub_term (name ) values" + "(\"term_test\" ) ");
            executeUpdate("insert into pub_term (name ) values" + "(\"term3\" )");
        } catch (Exception e) {
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
        assertEquals(2, termdb.countNonobsoleteTerms());
    }
