    public Collection<AsteriskChannel> getChannels() throws ManagerCommunicationException {
        initializeIfNeeded();
        return channelManager.getChannels();
    }
