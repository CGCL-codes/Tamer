    private static InputChannelInterface getChannelFromListByName(List<InputChannelInterface> channels, String channelInternalName) {
        for (final InputChannelInterface channel : channels) {
            if (channel.getInternalName().equals(channelInternalName)) return channel;
        }
        throw new RuntimeException("Channel not found");
    }
