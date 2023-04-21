    @SuppressWarnings("unused")
    private DirContext getNamedBindFromIdAut(String userName, String password) throws LdapException {
        String serverUrl = this.serverUrl + "/" + baseDN;
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, serverUrl);
        env.put("java.naming.ldap.version", "3");
        env.put(Context.SECURITY_PROTOCOL, "ssl");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, getUserDN(userName) + "," + baseDN);
        env.put(Context.SECURITY_CREDENTIALS, password);
        try {
            return new InitialDirContext(env);
        } catch (NamingException e) {
            if (log.isWarnEnabled()) {
                log.warn("transferFromIdAut: ", e);
            }
            throw new LdapException(e.getMessage(), "error_10");
        }
    }
