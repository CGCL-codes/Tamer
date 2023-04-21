    public static Properties getInstance(URL url) {
        try {
            InputStream is = url.openStream();
            return load(is);
        } catch (Exception e) {
            Logger.getRootLogger().error("Exception in check updates: " + e);
        }
        return null;
    }
