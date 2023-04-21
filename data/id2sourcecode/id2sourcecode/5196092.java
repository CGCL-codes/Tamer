    public InputSource getInputSource(String urlString) throws IOException {
        URL url = new URL(urlString);
        InputSource src = new InputSource(url.openStream());
        return src;
    }
