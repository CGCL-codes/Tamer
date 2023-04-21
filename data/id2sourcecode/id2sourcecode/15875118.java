    public void setChannelValues(Channel[] channels, int source) {
        Channel[] oldChannels = channelValues.getChannels(channels, source);
        if (source < 0) channelValues.updateChannels(channels, source);
        Channel[] newChannels = channelValues.getChannels(channels);
        try {
            dmxDevice.setValues(newChannels);
        } catch (IDMXDeviceException e) {
            channelValues.updateChannels(oldChannels, source);
            writeError(e);
            return;
        }
        connector.updateChannels(newChannels, channelValues.getChannelSources(channels));
    }
