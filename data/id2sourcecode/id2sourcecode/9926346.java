    private int getChannel(Instrument instrument) {
        int channel = 0;
        try {
            channel = channelMapping.get(instrument);
            return channel;
        } catch (Exception exc) {
            mapChannels();
            return getChannel(instrument);
        }
    }
