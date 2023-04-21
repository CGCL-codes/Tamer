    private Properties loadProperties(URL url) {
        Properties result = new Properties();
        if (url == null) return result;
        InputStream input = null;
        try {
            input = url.openStream();
            result.load(input);
        } catch (IOException e) {
            if (InternalPlatform.DEBUG_PREFERENCE_GENERAL) {
                Policy.debug("Problem opening stream to preference customization file: " + url);
                e.printStackTrace();
            }
        } finally {
            if (input != null) try {
                input.close();
            } catch (IOException e) {
            }
        }
        return result;
    }
