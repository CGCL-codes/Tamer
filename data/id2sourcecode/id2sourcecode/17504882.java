    private void setDimmerChannel(final int dimmerIndex, final int channelIndex) {
        Dimmer dimmer = oldShow.getDimmers().get(dimmerIndex);
        Channel channel = oldShow.getChannels().get(channelIndex);
        dimmer.setChannel(channel);
    }
