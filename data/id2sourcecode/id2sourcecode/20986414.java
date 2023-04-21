    public ITransactionalChannel getTransactionalChannel(IChannelKey key) throws XAwareException {
        if (key.getChannelKeyType() == IChannelKey.Type.JNDI) {
            createJtaTransactionIfNeeded(key);
        }
        ITransactionalChannel tc = managedChannels.get(key);
        if (tc != null) {
            lf.debug("TransactionalChannel found in transaction cache", CLASS_NAME, "getTransactionalChannel");
        }
        return tc;
    }
