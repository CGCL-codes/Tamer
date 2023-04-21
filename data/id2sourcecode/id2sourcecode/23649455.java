    public ChannelManager getChannelManager() {
        if (channelManager == null) {
            channelManager = new EthernetUDPChannelManager(UBIQUITOS_ETH_UDP_PORT, UBIQUITOS_ETH_UDP_CONTROL_PORT, UBIQUITOS_ETH_UDP_PASSIVE_PORT_RANGE);
        }
        return channelManager;
    }
