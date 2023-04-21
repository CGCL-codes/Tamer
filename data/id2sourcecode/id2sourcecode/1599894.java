    protected TGChannel getChannel(TGTrack track) {
        TGChannel tgChannel = this.manager.getChannel(track.getChannelId());
        if (tgChannel != null) {
            return tgChannel;
        }
        if (this.channelAux == null) {
            this.channelAux = this.manager.createChannel();
        }
        return this.channelAux;
    }
