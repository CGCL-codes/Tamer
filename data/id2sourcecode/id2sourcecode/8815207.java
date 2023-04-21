    public void load(URL url) throws ConfigurationException {
        if (url == null) {
            throw new ConfigurationException("URL cannot be null");
        }
        InputStream in = null;
        try {
            in = url.openStream();
            load(in);
        } catch (Exception e) {
            throw new ConfigurationException(e);
        } finally {
            IOUtilities.close(in);
        }
    }
