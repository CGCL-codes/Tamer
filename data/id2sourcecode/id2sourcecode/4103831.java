    public ClientConnection openActiveConnection(String networkDeviceName, String networkDeviceType) throws NetworkException {
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
        try {
            return channelManager.openActiveConnection(networkDeviceName);
        } catch (Exception e) {
            throw new NetworkException("Could not create channel.", e);
        }
    }
