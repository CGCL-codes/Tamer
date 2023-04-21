    public Object getChannelObject() throws XAwareException {
        if (connectionFactoryHolder == null) {
            try {
                connectionFactoryHolder = this.lookupConnectionFactory(bizViewContext);
            } catch (NamingException e) {
                lf.debug(e);
                throw new XAwareException(e);
            }
        }
        return connectionFactoryHolder;
    }
