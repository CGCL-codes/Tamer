    private TGTrack readTrack(int number, TGSong song) {
        int header = readHeader();
        TGTrack track = this.factory.newTrack();
        track.setNumber(number);
        track.setName(readUnsignedByteString());
        track.setSolo((header & TRACK_SOLO) != 0);
        track.setMute((header & TRACK_MUTE) != 0);
        readChannel(track.getChannel());
        int measureCount = song.countMeasureHeaders();
        TGMeasure lastMeasure = null;
        for (int i = 0; i < measureCount; i++) {
            TGMeasure measure = readMeasure(song.getMeasureHeader(i), lastMeasure);
            track.addMeasure(measure);
            lastMeasure = measure;
        }
        int stringCount = readByte();
        for (int i = 0; i < stringCount; i++) {
            track.getStrings().add(readInstrumentString(i + 1));
        }
        track.setOffset(TGTrack.MIN_OFFSET + readByte());
        readRGBColor(track.getColor());
        if (((header & TRACK_LYRICS) != 0)) {
            readLyrics(track.getLyrics());
        }
        return track;
    }
