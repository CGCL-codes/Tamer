    static ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        if ((baseName == null) || (locale == null) || (format == null) || (loader == null)) {
            throw new NullPointerException();
        }
        ResourceBundle bundle = null;
        if (!format.equals(XML)) {
            return null;
        }
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, format);
        URL url = loader.getResource(resourceName);
        if (url == null) {
            return null;
        }
        URLConnection connection = url.openConnection();
        if (connection == null) {
            return null;
        }
        if (reload) {
            connection.setUseCaches(false);
        }
        InputStream stream = connection.getInputStream();
        if (stream == null) {
            return null;
        }
        BufferedInputStream bis = new BufferedInputStream(stream);
        bundle = new XMLResourceBundle(bis);
        bis.close();
        return bundle;
    }
