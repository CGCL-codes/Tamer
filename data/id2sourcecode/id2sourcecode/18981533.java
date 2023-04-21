    public URLInputCursor(Converter<? extends E> converter, URL url, int bufferSize) throws IOException {
        super(new DataInputStream(new BufferedInputStream(url.openStream(), bufferSize)), converter);
    }
