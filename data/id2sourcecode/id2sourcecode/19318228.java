    private String getUserDN(String userIdNumber) throws LdapException {
        String userDNreturn = "";
        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration persons = null;
        try {
            persons = this.getAnonymousBindFromIdAut().search("", "uidNumber=" + userIdNumber, sc);
            while (persons.hasMore()) {
                SearchResult sr = (SearchResult) persons.next();
                userDNreturn = sr.getName();
                break;
            }
        } catch (NamingException e) {
            if (log.isWarnEnabled()) {
                log.warn("transferFromIdAut: ", e);
            }
            throw new LdapException(e.getMessage(), "error_10");
        }
        return userDNreturn;
    }
