    private static InputStream loadFromUrl(String urlPath) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Opening url [" + urlPath + "]...");
        }
        URL url = new URL(urlPath);
        return url.openStream();
    }
