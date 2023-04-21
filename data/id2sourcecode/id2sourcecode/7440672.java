    public TGSong newSong() {
        TGSong song = getFactory().newSong();
        TGMeasureHeader header = getFactory().newHeader();
        header.setNumber(1);
        header.setStart(TGDuration.QUARTER_TIME);
        header.getTimeSignature().setNumerator(4);
        header.getTimeSignature().getDenominator().setValue(TGDuration.QUARTER);
        song.addMeasureHeader(header);
        TGMeasure measure = getFactory().newMeasure(header);
        TGTrack track = getFactory().newTrack();
        track.setNumber(1);
        track.setName("Track 1");
        track.addMeasure(measure);
        track.getChannel().setChannel((short) 0);
        track.getChannel().setEffectChannel((short) 1);
        track.setStrings(createDefaultInstrumentStrings());
        TGColor.RED.copy(track.getColor());
        song.addTrack(track);
        return song;
    }
