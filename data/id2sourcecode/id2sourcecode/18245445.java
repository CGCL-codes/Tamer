    public VersionReader(String urlString) {
        try {
            URL url = new URL(EnvConstants.LAST_REVISION_CHECK_URL);
            InputStream is = url.openStream();
            load(is);
        } catch (IOException e) {
            logger.error("Exception in load version file: " + e, e);
            setVersion("");
            setRelease(0);
        }
    }
