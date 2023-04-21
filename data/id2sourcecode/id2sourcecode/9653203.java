    public void write(Reader reader, long length) throws IOException {
        InputStream inputStream = new ReaderInputStream(reader);
        write(inputStream, length * 2);
    }
