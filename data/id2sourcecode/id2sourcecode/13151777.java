    public AsteriskChannel getChannelByName(String name) throws ManagerCommunicationException {
        initializeIfNeeded();
        return channelManager.getChannelImplByName(name);
    }
