    public InputStream getMessageAsStream(int index) throws IOException {
        long position = getMessagePosition(index);
        int size;
        log.debug("MboxFile.getMessageAsStream(" + String.valueOf(position) + ")");
        if (index < messagePositions.length - 1) {
            size = (int) (messagePositions[index + 1] - position);
        } else {
            size = (int) (getChannel().size() - position);
        }
        ByteBuffer byFrom = getChannel().map(FileChannel.MapMode.READ_ONLY, position, 256);
        CharBuffer chFrom = decoder.decode(byFrom);
        int start = 0;
        char c = chFrom.charAt(start);
        while (c == ' ' || c == '\r' || c == '\n' || c == '\t') c = chFrom.charAt(++start);
        if (!chFrom.subSequence(start, start + FROM__PREFIX.length()).toString().equals(FROM__PREFIX)) throw new IOException("MboxFile.getMessageAsStream() starting position " + String.valueOf(start) + " \"" + chFrom.subSequence(start, start + FROM__PREFIX.length()).toString() + "\" does not match a begin message token \"" + FROM__PREFIX + "\"");
        while (chFrom.charAt(start++) != (char) 10) ;
        log.debug("  skip = " + String.valueOf(start));
        log.debug("  start = " + String.valueOf(position + start));
        MappedByteBuffer byBuffer = getChannel().map(FileChannel.MapMode.READ_ONLY, position + start, size - start);
        byte[] byArray = new byte[size - start];
        byBuffer.get(byArray);
        ByteArrayInputStream byStrm = new ByteArrayInputStream(byArray);
        return byStrm;
    }
