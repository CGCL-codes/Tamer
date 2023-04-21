    public InputStream getPartAsStream(final long begin, final long offset, final int size) throws IOException {
        log.debug("MboxFile.getPartAsStream(" + String.valueOf(begin) + "," + String.valueOf(offset) + "," + String.valueOf(size) + ")");
        ByteBuffer byFrom = getChannel().map(FileChannel.MapMode.READ_ONLY, begin, 128);
        CharBuffer chFrom = decoder.decode(byFrom);
        log.debug("from line decoded");
        int start = 0;
        char c = chFrom.charAt(start);
        while (c == ' ' || c == '\r' || c == '\n' || c == '\t') c = chFrom.charAt(++start);
        log.debug("first line is " + chFrom.subSequence(start, start + FROM__PREFIX.length()).toString());
        if (!chFrom.subSequence(start, start + FROM__PREFIX.length()).toString().equals(FROM__PREFIX)) throw new IOException("MboxFile.getPartAsStream() starting position " + String.valueOf(start) + " \"" + chFrom.subSequence(start, start + FROM__PREFIX.length()).toString() + "\" does not match a begin message token \"" + FROM__PREFIX + "\"");
        while (chFrom.charAt(start++) != (char) 10) ;
        start += offset;
        log.debug("  skip = " + String.valueOf(start));
        log.debug("  start = " + String.valueOf(start));
        MappedByteBuffer byBuffer = getChannel().map(FileChannel.MapMode.READ_ONLY, begin + start, size);
        byte[] byArray = new byte[size];
        byBuffer.get(byArray);
        ByteArrayInputStream byStrm = new ByteArrayInputStream(byArray);
        return byStrm;
    }
