    public Channel getChannel(String tag) {
        ChannelWindow cw = getChannelWindow(tag);
        return null != cw ? cw.getChannel() : null;
    }
