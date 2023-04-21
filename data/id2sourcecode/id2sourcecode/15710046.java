    @Parameters({ "dsmlSearchDn", "dsmlSearchFilter" })
    @Test(groups = { "dsml" })
    public void searchAndCompareDsmlv1(final String dn, final String filter) throws Exception {
        final Connection conn = TestUtil.createConnection();
        try {
            conn.open();
            final SearchOperation search = new SearchOperation(conn);
            final LdapResult result1 = search.execute(new SearchRequest(dn, new SearchFilter(filter))).getResult();
            final StringWriter writer = new StringWriter();
            final Dsmlv1Writer dsmlWriter = new Dsmlv1Writer(writer);
            dsmlWriter.write(result1);
            final StringReader reader = new StringReader(writer.toString());
            final Dsmlv1Reader dsmlReader = new Dsmlv1Reader(reader);
            final LdapResult result2 = dsmlReader.read();
            AssertJUnit.assertEquals(result1, result2);
        } finally {
            conn.close();
        }
    }
