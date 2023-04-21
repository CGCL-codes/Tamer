    private SecureResourceBundle getResourceBundle(String path) throws IOException {
        URL url = getClass().getResource(path);
        if (url == null) {
            path = path.substring(0, path.lastIndexOf(".")) + ".en";
            url = getClass().getResource(path);
        }
        return new SecureResourceBundle(url.openStream());
    }
