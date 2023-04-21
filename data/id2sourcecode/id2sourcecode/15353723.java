    public static final void copy(Writer writer, Reader reader) throws IOException {
        final char[] buf = new char[1024 * 4];
        for (int v; (v = reader.read(buf)) >= 0; ) {
            if (v > 0) writer.write(buf, 0, v);
        }
    }
