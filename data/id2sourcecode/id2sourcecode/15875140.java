    public boolean disableNetworkInput() {
        inputDevice.stop();
        inputDevice = null;
        Channel[] channels = channelValues.getChannels(ChannelValues.NETWORK_SOURCE);
        channelValues.removeSet(ChannelValues.NETWORK_SOURCE);
        connector.updateChannels(channelValues.getChannels(channels), channelValues.getChannelSources(channels));
        return true;
    }
