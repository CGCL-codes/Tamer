    private StreamConnectionThreaded[] openStreamChannel(UpDevice device, ServiceCall serviceCall, UOSMessageContext messageContext) throws ServiceCallException {
        StreamConnectionThreaded[] streamConnectionThreadeds = null;
        try {
            String netType = null;
            if (serviceCall.getChannelType() != null) {
                netType = serviceCall.getChannelType();
            } else {
                UpNetworkInterface network = this.connectivityManager.getAppropriateInterface(device, serviceCall);
                netType = network.getNetType();
            }
            int channels = serviceCall.getChannels();
            streamConnectionThreadeds = new StreamConnectionThreaded[channels];
            String[] channelIDs = new String[channels];
            for (int i = 0; i < channels; i++) {
                NetworkDevice networkDevice = connectionManagerControlCenter.getAvailableNetworkDevice(netType);
                channelIDs[i] = connectionManagerControlCenter.getChannelID(networkDevice.getNetworkDeviceName());
                StreamConnectionThreaded streamConnectionThreaded = new StreamConnectionThreaded(messageContext, networkDevice);
                streamConnectionThreaded.start();
                streamConnectionThreadeds[i] = streamConnectionThreaded;
            }
            serviceCall.setChannelIDs(channelIDs);
            serviceCall.setChannelType(netType);
        } catch (Exception e) {
            throw new ServiceCallException(e);
        }
        return streamConnectionThreadeds;
    }
