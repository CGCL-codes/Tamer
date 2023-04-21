    public Map<String, String[]> getPersonsValues(String userDN) throws LdapException {
        Map<String, String[]> retMap = new HashMap<String, String[]>();
        DirContext context;
        try {
            context = this.getAnonymousBindFromIdAut();
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration persons = context.search(userDN, "(objectclass=*)", sc);
            while (persons != null && persons.hasMore()) {
                SearchResult sr = (SearchResult) persons.next();
                Attributes attributes = sr.getAttributes();
                NamingEnumeration enu = attributes.getAll();
                while (enu.hasMore()) {
                    Attribute attr = (Attribute) enu.next();
                    String[] arr = new String[attr.size()];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (String) attr.get(i);
                    }
                    retMap.put(attr.getID().toLowerCase(), arr);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("found " + retMap.size() + " attributes from idauth for: " + userDN);
            }
        } catch (NamingException e) {
            if (log.isWarnEnabled()) {
                log.warn("transferFromIdAut: ", e);
            }
            if (e.getMessage().equals("[LDAP: error code 49 - Invalid Credentials]")) {
                throw new LdapException(e.getMessage(), "error_01");
            }
            if (e.getMessage().equals("[LDAP: error code 53 - unauthenticated bind (DN with no password) disallowed]")) {
                throw new LdapException(e.getMessage(), "error_02");
            }
            if (e.getMessage().equals("[LDAP: error code 34 - invalid DN]")) {
                throw new LdapException("wrong username");
            }
        }
        return retMap;
    }
