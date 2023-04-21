    public TGChannel getUsedChannel(int channel) {
        for (int i = 0; i < getSong().countTracks(); i++) {
            TGTrack track = getSong().getTrack(i);
            if (channel == track.getChannel().getChannel()) {
                return track.getChannel().clone(getFactory());
            }
        }
        return null;
    }
