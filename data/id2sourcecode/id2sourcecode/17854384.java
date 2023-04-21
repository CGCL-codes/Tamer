    public void translateStringToFile(final String s, final File file, final boolean append, final boolean writeSeparator) throws IOException, InvalidSymbolException {
        final int slen = s.length();
        RandomAccessFile f = new RandomAccessFile(file, "rw");
        FileChannel fcout = f.getChannel();
        long flen = fcout.size();
        long start = append ? flen : 0;
        if (flen > 0 && append && writeSeparator) {
            f.seek(flen - 1);
            if (f.readByte() != this.codeSeparator()) {
                f.writeByte(this.codeSeparator());
                flen++;
            }
        }
        long newlen = append ? (flen + slen) : slen;
        if (writeSeparator) newlen++;
        f.seek(newlen - 1);
        f.writeByte(0);
        f.seek(start);
        MappedByteBuffer buf = fcout.map(MapMode.READ_WRITE, start, newlen - start);
        for (int i = 0; i < slen; i++) buf.put(this.code((byte) s.charAt(i)));
        if (writeSeparator) buf.put(this.codeSeparator());
        fcout.close();
        f.close();
    }
