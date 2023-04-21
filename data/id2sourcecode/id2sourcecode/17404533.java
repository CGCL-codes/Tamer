    private void write(TGSong song) throws IOException {
        writeUnsignedByteString(song.getName());
        writeUnsignedByteString(song.getArtist());
        writeUnsignedByteString(song.getAlbum());
        writeUnsignedByteString(song.getAuthor());
        writeUnsignedByteString(song.getDate());
        writeUnsignedByteString(song.getCopyright());
        writeUnsignedByteString(song.getWriter());
        writeUnsignedByteString(song.getTranscriber());
        writeIntegerString(song.getComments());
        writeByte(song.countChannels());
        for (int i = 0; i < song.countChannels(); i++) {
            writeChannel(song.getChannel(i));
        }
        writeShort((short) song.countMeasureHeaders());
        TGMeasureHeader lastHeader = null;
        Iterator headers = song.getMeasureHeaders();
        while (headers.hasNext()) {
            TGMeasureHeader header = (TGMeasureHeader) headers.next();
            writeMeasureHeader(header, lastHeader);
            lastHeader = header;
        }
        writeByte(song.countTracks());
        for (int i = 0; i < song.countTracks(); i++) {
            writeTrack(song.getTrack(i));
        }
    }
