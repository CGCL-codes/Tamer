    public void setChannelValue(Channel channel, int source) {
        if (source < 0) channelValues.updateChannel(channel, source);
        try {
            dmxDevice.setValue(channelValues.getChannel(channel));
        } catch (IDMXDeviceException e) {
            writeError(e);
            return;
        }
        connector.updateChannel(channelValues.getChannel(channel), channelValues.getChannelSource(channel));
    }
