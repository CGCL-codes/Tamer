    private static InputStream loadFromUrl(String urlPath) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Opening url [" + urlPath + "]...");
        }
        URL url = new URL(urlPath);
        return url.openStream();
    }
