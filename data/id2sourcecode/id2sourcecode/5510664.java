    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        if (baseName == null || locale == null || format == null || loader == null) throw new NullPointerException();
        ResourceBundle bundle = null;
        if (format.equals(FORMAT_XML)) {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = i18NPath + "/" + toResourceName(bundleName, format);
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                BufferedInputStream bis = new BufferedInputStream(stream);
                bundle = new XmlResourceBundle(bis);
                bis.close();
            }
        }
        return bundle;
    }
