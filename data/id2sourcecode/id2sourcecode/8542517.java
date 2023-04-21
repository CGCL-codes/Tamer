    private static InputChannelInterface getChannelFromListByName(List<InputChannelItemInterface> channels, String channelInternalName) {
        for (Iterator<InputChannelItemInterface> it = channels.iterator(); it.hasNext(); ) {
            final InputChannelInterface channel = (InputChannelInterface) it.next();
            if (channel.getInternalName().equals(channelInternalName)) return channel;
        }
        return null;
    }
