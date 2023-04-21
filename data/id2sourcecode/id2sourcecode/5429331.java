    private SecureResourceBundle getResourceBundle(String path) {
        InputStream in = null;
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                path = path.substring(0, path.lastIndexOf(".")) + ".en";
                url = getClass().getResource(path);
            }
            in = url.openStream();
        } catch (Exception ex) {
            log.error("Can't find URL to: <" + path + "> " + ex.getMessage(), ex);
        }
        return new SecureResourceBundle(in);
    }
