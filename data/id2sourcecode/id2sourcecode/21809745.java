    public static void debugShowStcoInfo(RandomAccessFile raf) throws IOException, CannotReadException {
        Mp4BoxHeader moovHeader = Mp4BoxHeader.seekWithinLevel(raf, Mp4NotMetaFieldKey.MOOV.getFieldName());
        if (moovHeader == null) {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        ByteBuffer moovBuffer = ByteBuffer.allocate(moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
        raf.getChannel().read(moovBuffer);
        moovBuffer.rewind();
        Mp4BoxHeader boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.MVHD.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        ByteBuffer mvhdBuffer = moovBuffer.slice();
        Mp4MvhdBox mvhd = new Mp4MvhdBox(boxHeader, mvhdBuffer);
        mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.TRAK.getFieldName());
        int endOfFirstTrackInBuffer = mvhdBuffer.position() + boxHeader.getDataLength();
        if (boxHeader == null) {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.MDIA.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.MDHD.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.MINF.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.SMHD.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.STBL.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.STCO.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        Mp4StcoBox stco = new Mp4StcoBox(boxHeader, mvhdBuffer);
        stco.printAlloffsets();
    }
