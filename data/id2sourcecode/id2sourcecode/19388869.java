    public LDAPTemplate getLDAPTemplate(IBizDriver bizDriver, ITransactionContext transactionContext) throws XAwareException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        final IChannelKey key = bizDriver.getChannelSpecificationKey();
        LDAPTemplate ldapTemplate = null;
        ContextSource contextSource = contextSourcePool.get(key);
        if (contextSource == null) {
            contextSource = (ContextSource) bizDriver.createChannelObject();
            contextSourcePool.put(key, contextSource);
        }
        ldapTemplate = new LDAPTemplate(contextSource, null);
        return ldapTemplate;
    }
