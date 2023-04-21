    private InputSource getResultInputStream() throws JGDError, IOException {
        String urlString = getURL();
        URL url = new URL(urlString);
        InputStream is = url.openStream();
        InputSource iSource = new InputSource(getFilteredStream(is));
        return iSource;
    }
