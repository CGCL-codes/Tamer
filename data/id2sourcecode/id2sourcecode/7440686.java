    private TGTrack makeNewTrack() {
        TGTrack track = getFactory().newTrack();
        track.setNumber(getNextTrackNumber());
        track.setName("Track " + track.getNumber());
        Iterator it = getSong().getMeasureHeaders();
        while (it.hasNext()) {
            TGMeasureHeader header = (TGMeasureHeader) it.next();
            TGMeasure measure = getFactory().newMeasure(header);
            track.addMeasure(measure);
        }
        track.setStrings(createDefaultInstrumentStrings());
        getFreeChannel(TGChannel.DEFAULT_INSTRUMENT, false).copy(track.getChannel());
        TGColor.RED.copy(track.getColor());
        return track;
    }
