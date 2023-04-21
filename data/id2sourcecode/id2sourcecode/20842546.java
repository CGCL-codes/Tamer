    @Parameters({ "ldifSearchDn", "ldifSearchFilter" })
    @Test(groups = { "ldif" })
    public void searchAndCompareLdif(final String dn, final String filter) throws Exception {
        final Connection conn = TestUtil.createConnection();
        try {
            conn.open();
            final SearchOperation search = new SearchOperation(conn);
            final LdapResult result1 = search.execute(new SearchRequest(dn, new SearchFilter(filter))).getResult();
            final StringWriter writer = new StringWriter();
            final LdifWriter ldifWriter = new LdifWriter(writer);
            ldifWriter.write(result1);
            final StringReader reader = new StringReader(writer.toString());
            final LdifReader ldifReader = new LdifReader(reader);
            final LdapResult result2 = ldifReader.read();
            AssertJUnit.assertEquals(result1, result2);
        } finally {
            conn.close();
        }
    }
