    public void copy(TGFactory factory, TGSong song, TGTrack track) {
        track.clear();
        track.setNumber(getNumber());
        track.setName(getName());
        track.setOffset(getOffset());
        getChannel().copy(track.getChannel());
        getColor().copy(track.getColor());
        getLyrics().copy(track.getLyrics());
        for (int i = 0; i < getStrings().size(); i++) {
            TGString string = (TGString) getStrings().get(i);
            track.getStrings().add(string.clone(factory));
        }
        for (int i = 0; i < countMeasures(); i++) {
            TGMeasure measure = getMeasure(i);
            track.addMeasure(measure.clone(factory, song.getMeasureHeader(i)));
        }
    }
