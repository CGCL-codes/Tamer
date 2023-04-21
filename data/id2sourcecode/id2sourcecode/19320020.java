    public static Track copy(Track track) {
        Track copy = new Track();
        copy.setTitle(new String(track.getTitle()));
        copy.setChannel(track.getChannel());
        copy.setInstrument(track.getInstrument());
        for (int i = 0; i < track.getClips().size(); i++) {
            copy.getClips().add(copy(track.getClips().get(i)));
        }
        return copy;
    }
