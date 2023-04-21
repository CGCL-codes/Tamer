    void createURLReader(URL url) throws IOException {
        reader = new InputStreamReader(url.openStream());
        readerType = READER_READER;
        readerURL = url;
        buffer = new char[BUFSIZE];
        bufferPos = BUFSIZE + 1;
        bufferLen = 0;
        line = 1;
        column = 1;
    }
