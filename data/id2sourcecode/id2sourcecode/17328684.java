    public InputStream getContentAsStream(String key) {
        if (logger.isDebugEnabled()) logger.debug("getContentAsStream(" + key + ")");
        URL url = getURL(key);
        try {
            return url.openStream();
        } catch (IOException ioe) {
            throw new ResourceNotFoundException("Cannot connect to URL: " + url);
        }
    }
