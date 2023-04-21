    protected String readValueStream(Reader in, int streamSize, int bufSize) throws IOException {
        char[] buf = new char[bufSize];
        int read;
        StringWriter out = (streamSize > 0) ? new StringWriter(streamSize) : new StringWriter();
        try {
            while ((read = in.read(buf, 0, bufSize)) >= 0) {
                out.write(buf, 0, read);
            }
            return out.toString();
        } finally {
            in.close();
        }
    }
