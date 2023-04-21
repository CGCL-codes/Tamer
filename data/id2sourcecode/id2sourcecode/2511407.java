    protected static void writeReader(Reader reader, Writer out) throws IOException {
        final int bufferSize = 1024 * 4;
        char[] buffer = new char[bufferSize];
        int read = -1;
        while ((read = reader.read(buffer, 0, bufferSize)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
    }
