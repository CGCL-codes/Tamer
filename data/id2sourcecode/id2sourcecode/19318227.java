    public boolean isUserNameInContainer(String userName) throws LdapException {
        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration persons;
        try {
            persons = this.getAnonymousBindFromIdAut().search("", "uid=" + userName, sc);
            while (persons.hasMore()) {
                SearchResult sr = (SearchResult) persons.next();
                String tempStr = sr.getName();
                if (tempStr.indexOf(userName) > 0) {
                    return true;
                }
            }
        } catch (NamingException e) {
            if (log.isWarnEnabled()) {
                log.warn("transferFromIdAut: ", e);
            }
            throw new LdapException(e.getMessage(), "error_10");
        }
        return false;
    }
