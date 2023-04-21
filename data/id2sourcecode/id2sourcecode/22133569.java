    private int resolveChannel(GMChannelRoute gmChannel, boolean bendMode) {
        return (bendMode ? gmChannel.getChannel2() : gmChannel.getChannel1());
    }
