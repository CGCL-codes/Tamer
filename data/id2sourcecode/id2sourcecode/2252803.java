    protected URLConnection openConnection(final URL url) throws IOException {
        return new PlaybackURLConnection(url);
    }
