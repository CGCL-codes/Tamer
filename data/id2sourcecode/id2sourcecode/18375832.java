    public InputChannelItemInterface getChannel(int index) {
        if (index >= channelBindings.size()) return null;
        return channelBindings.get(index).getBoundChannel();
    }
