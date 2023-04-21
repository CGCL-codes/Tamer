    @Override
    protected HttpURLConnection openConnection(String path, String query) throws IOException {
        try {
            final URL url = new URI("https", null, nnAddr.getHostName(), nnAddr.getPort(), path, query, null).toURL();
            return (HttpURLConnection) url.openConnection();
        } catch (URISyntaxException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }
