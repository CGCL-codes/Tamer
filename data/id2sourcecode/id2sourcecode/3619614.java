    public static InputStream getConfigStream(URL url) throws Exception {
        try {
            checkJAXPAvailability();
            return url.openStream();
        } catch (Exception ex) {
            throw createChannelConfigurationException(ex);
        }
    }
