    private void process(Reader is, Writer os) throws IOException {
        char[] buffer = new char[sChunk];
        int length;
        while ((length = is.read(buffer, 0, sChunk)) != -1) os.write(buffer, 0, length);
    }
