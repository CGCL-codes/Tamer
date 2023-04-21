    @Override
    protected URLConnection openConnection(final URL url) {
        return new DataURLConnection(url);
    }
