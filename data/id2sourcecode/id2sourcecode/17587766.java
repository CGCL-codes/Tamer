    protected URLConnection openConnection(final URL url) throws IOException {
        return new ArtifactURLConnection(url);
    }
