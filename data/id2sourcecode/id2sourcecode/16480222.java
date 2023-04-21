    public Object getChannelObject() throws XAwareException {
        if (contextSource == null) {
            try {
                contextSource = this.lookupContextSource(bizViewContext);
            } catch (NamingException namingException) {
                logger.debug(namingException);
                throw new XAwareException(namingException);
            }
        }
        return this.contextSource;
    }
