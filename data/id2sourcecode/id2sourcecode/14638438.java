    public ChannelManager getChannelManager() {
        if (channelManager == null) {
            channelManager = new BluetoothChannelManager(btUtil, UBIQUITOS_BTH_CLIENT);
        }
        return channelManager;
    }
