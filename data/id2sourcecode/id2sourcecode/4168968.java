    public void testRollBackWithError() throws Exception {
        int countTerms = termdb.countNonobsoleteTerms();
        conn.setAutoCommit(false);
        try {
            executeUpdate("insert into pub_term (name ) values" + "(\"term_test\" ) ");
            executeUpdate("insert into pub_term (name2 ) values" + "(\"term3\" )");
        } catch (Exception e) {
            Log.getLogger(this.getClass()).debug(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
        assertEquals(countTerms, termdb.countNonobsoleteTerms());
    }
