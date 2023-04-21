    public Object getChannelObject() throws XAwareException {
        if (ds == null) {
            try {
                ds = this.lookupDataSource(bizViewContext);
            } catch (NamingException e) {
                lf.debug(e);
                throw new XAwareException(e);
            }
        }
        return this.ds;
    }
