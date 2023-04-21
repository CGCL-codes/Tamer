    public static StringBuffer getContent(final URL url) throws IOException {
        final StringBuffer buffer = new StringBuffer();
        final InputStreamReader reader = new InputStreamReader(url.openStream());
        for (int numRead = 0; numRead >= 0; ) {
            int offset = 0;
            for (; offset < _readBuffer.length && (numRead = reader.read(_readBuffer, offset, _readBuffer.length - offset)) >= 0; offset += numRead) ;
            buffer.append(_readBuffer, 0, offset);
        }
        reader.close();
        return buffer;
    }
