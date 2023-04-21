    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new Connection(url);
    }
