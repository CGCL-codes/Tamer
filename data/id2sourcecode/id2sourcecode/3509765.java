    protected HttpURLConnection openConnection(String path, String query) throws IOException {
        try {
            final URL url = new URI("http", null, nnAddr.getHostName(), nnAddr.getPort(), path, query, null).toURL();
            if (LOG.isTraceEnabled()) {
                LOG.trace("url=" + url);
            }
            return (HttpURLConnection) url.openConnection();
        } catch (URISyntaxException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }
