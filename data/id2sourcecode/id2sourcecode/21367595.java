    protected Reader(URL url) throws IOException {
        super(url.openStream());
        readerposition = 0;
    }
