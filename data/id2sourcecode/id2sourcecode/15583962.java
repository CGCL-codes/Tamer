    private Channel getChannel(final int row) {
        int[] channelIndexes = frame.getChannelsNotInGroup();
        int channelIndex = channelIndexes[row];
        Channel channel = frame.getContext().getShow().getChannels().get(channelIndex);
        return channel;
    }
