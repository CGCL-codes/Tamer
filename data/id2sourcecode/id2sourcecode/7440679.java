    public void updateChannel(TGChannel channel) {
        for (int i = 0; i < getSong().countTracks(); i++) {
            TGTrack track = getSong().getTrack(i);
            if (channel.getChannel() == track.getChannel().getChannel()) {
                track.setChannel(channel.clone(getFactory()));
            }
        }
    }
