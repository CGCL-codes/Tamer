    private boolean isPercussionChannel(TGSong song, int channelId) {
        Iterator it = song.getChannels();
        while (it.hasNext()) {
            TGChannel channel = (TGChannel) it.next();
            if (channel.getChannelId() == channelId) {
                return channel.isPercussionChannel();
            }
        }
        return false;
    }
