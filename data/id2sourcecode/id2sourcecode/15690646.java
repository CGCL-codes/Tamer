    protected InputStream _optimizedStreamFromURL(URL url) throws IOException {
        if ("file".equals(url.getProtocol())) {
            String host = url.getHost();
            if (host == null || host.length() == 0) {
                return new FileInputStream(url.getPath());
            }
        }
        return url.openStream();
    }
