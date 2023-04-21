    public boolean[] getUsedChannels() {
        boolean[] channels = new boolean[MAX_CHANNELS];
        for (int i = 0; i < getSong().countTracks(); i++) {
            TGTrack track = getSong().getTrack(i);
            channels[track.getChannel().getChannel()] = true;
        }
        return channels;
    }
