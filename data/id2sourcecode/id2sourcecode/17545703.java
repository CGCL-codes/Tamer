    public static void copy(Reader reader, Writer writer) throws IOException {
        if (null == reader) {
            throw new NullPointerException("reader is null");
        }
        if (null == writer) {
            throw new NullPointerException("writer is null");
        }
        Reader rdr = new BufferedReader(reader);
        Writer w = new BufferedWriter(writer);
        final int bufSize = 1024;
        final char[] buf = new char[bufSize];
        int r;
        do {
            r = rdr.read(buf);
            if (r > 0) {
                w.write(buf, 0, r);
            }
        } while (r >= 0);
        w.flush();
    }
