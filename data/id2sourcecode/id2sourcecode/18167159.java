    private void writeTrack(TGTrack track) {
        int header = 0;
        if (track.isSolo()) {
            header |= TRACK_SOLO;
        }
        if (track.isMute()) {
            header |= TRACK_MUTE;
        }
        if (!track.getLyrics().isEmpty()) {
            header |= TRACK_LYRICS;
        }
        writeHeader(header);
        writeUnsignedByteString(track.getName());
        writeChannel(getChannel(track.getSong(), track));
        TGMeasure lastMeasure = null;
        Iterator measures = track.getMeasures();
        while (measures.hasNext()) {
            TGMeasure measure = (TGMeasure) measures.next();
            writeMeasure(measure, lastMeasure);
            lastMeasure = measure;
        }
        writeByte(track.getStrings().size());
        Iterator stringIt = track.getStrings().iterator();
        while (stringIt.hasNext()) {
            TGString string = (TGString) stringIt.next();
            writeInstrumentString(string);
        }
        writeByte(track.getOffset() - TGTrack.MIN_OFFSET);
        writeRGBColor(track.getColor());
        if (((header & TRACK_LYRICS) != 0)) {
            writeLyrics(track.getLyrics());
        }
    }
