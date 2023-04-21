    protected Properties loadPropertiesFromURL(URL propertiesFileUrl) {
        if (propertiesFileUrl == null) {
            return null;
        }
        InputStream urlStream = null;
        try {
            urlStream = propertiesFileUrl.openStream();
            return loadPropertiesFromStream(urlStream);
        } catch (IOException e) {
            throw new DbMaintainException("Unable to load configuration file " + propertiesFileUrl, e);
        } finally {
            closeQuietly(urlStream);
        }
    }
