    private TGChannel getChannel(TGSong song, TGTrack track) {
        TGSongManager tgSongManager = new TGSongManager(this.factory);
        tgSongManager.setSong(song);
        TGChannel tgChannel = tgSongManager.getChannel(track.getChannelId());
        if (tgChannel == null) {
            tgChannel = this.factory.newChannel();
        }
        return tgChannel;
    }
