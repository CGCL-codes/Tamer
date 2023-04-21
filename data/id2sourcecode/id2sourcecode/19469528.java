    public InputStream getInputStream(URL url) throws IOException {
        logger.info("getInputStream called");
        return getInputStream(null, url.openConnection());
    }
