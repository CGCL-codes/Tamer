    public NetworkDevice getAvailableNetworkDevice(String networkDeviceType) throws NetworkException {
        ChannelManager channelManager = null;
        for (ConnectionManager cm : connectionManagersList) {
            if (cm.getNetworkDevice().getNetworkDeviceType().equals(networkDeviceType)) {
                channelManager = cm.getChannelManager();
                break;
            }
        }
        if (channelManager == null) {
            throw new NetworkException("There is no Channel Manager for the given connection type: " + networkDeviceType);
        }
        return channelManager.getAvailableNetworkDevice();
    }
