    private DirContext getAnonymousBindFromIdAut() throws LdapException {
        String serverUrl = this.serverUrl + "/" + baseDN;
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, serverUrl);
        env.put("java.naming.ldap.version", "3");
        env.put(Context.SECURITY_PROTOCOL, "ssl");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        try {
            return new InitialDirContext(env);
        } catch (NamingException e) {
            if (log.isWarnEnabled()) {
                log.warn("transferFromIdAut: ", e);
            }
            throw new LdapException(e.getMessage(), "error_10");
        }
    }
