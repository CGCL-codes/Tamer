    @Parameters({ "ldifSearchDn", "ldifSearchFilter" })
    @Test(groups = { "ldiftest" })
    public void searchAndCompareLdif(final String dn, final String filter) throws Exception {
        final Ldap ldap = TestUtil.createLdap();
        final Ldif ldif = new Ldif();
        final Iterator<SearchResult> iter = ldap.search(dn, new SearchFilter(filter));
        final LdapResult result1 = TestUtil.newLdapResult(iter);
        final StringWriter writer = new StringWriter();
        ldif.outputLdif(result1.toSearchResults().iterator(), writer);
        final StringReader reader = new StringReader(writer.toString());
        final LdapResult result2 = TestUtil.newLdapResult(ldif.importLdif(reader));
        AssertJUnit.assertEquals(result1, result2);
        ldap.close();
    }
