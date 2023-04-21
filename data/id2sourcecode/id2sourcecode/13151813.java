    public AsteriskChannel getChannelByNameAndActive(String name) throws ManagerCommunicationException {
        initializeIfNeeded();
        return channelManager.getChannelImplByNameAndActive(name);
    }
