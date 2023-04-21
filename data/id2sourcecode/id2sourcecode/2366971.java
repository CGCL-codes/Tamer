    @Parameters({ "dsmlSearchDn", "dsmlSearchFilter" })
    @Test(groups = { "dsmltest" })
    public void searchAndCompareDsmlv1(final String dn, final String filter) throws Exception {
        final Ldap ldap = TestUtil.createLdap();
        final Dsmlv1 dsml = new Dsmlv1();
        final Iterator<SearchResult> iter = ldap.search(dn, new SearchFilter(filter));
        final LdapResult result1 = TestUtil.newLdapResult(iter);
        final StringWriter writer = new StringWriter();
        dsml.outputDsml(result1.toSearchResults().iterator(), writer);
        final StringReader reader = new StringReader(writer.toString());
        final LdapResult result2 = dsml.importDsmlToLdapResult(reader);
        AssertJUnit.assertEquals(result1, result2);
        ldap.close();
    }
