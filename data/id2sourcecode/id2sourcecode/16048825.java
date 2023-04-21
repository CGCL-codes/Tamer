    public ChannelIdentification getChannel(int lcn) {
        ChannelIdentification channel = this.channels.get(lcn);
        if (channel != null) return channel;
        for (NetworkIdentification ni : this.networks) {
            channel = ni.getChannel(lcn);
            if (channel != null) return channel;
        }
        return null;
    }
