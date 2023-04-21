    public AsteriskChannel getChannelById(String id) throws ManagerCommunicationException {
        initializeIfNeeded();
        return channelManager.getChannelImplById(id);
    }
