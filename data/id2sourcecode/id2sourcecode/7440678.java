    public int countTracksForChannel(int channel) {
        int count = 0;
        for (int i = 0; i < getSong().countTracks(); i++) {
            TGTrack track = getSong().getTrack(i);
            if (channel == track.getChannel().getChannel()) {
                count++;
            }
        }
        return count;
    }
