    @Override
    public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        if (baseName == null || locale == null || format == null || loader == null) {
            throw new IllegalArgumentException();
        }
        if (!format.equals("xml")) {
            return null;
        }
        ResourceBundle bundle = null;
        final String resourceName = toResourceName(toBundleName(baseName, locale), format);
        InputStream stream = null;
        if (reload) {
            final URL url = loader.getResource(resourceName);
            if (url == null) {
                return null;
            }
            final URLConnection connection = url.openConnection();
            if (connection == null) {
                return null;
            }
            connection.setUseCaches(false);
            stream = connection.getInputStream();
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (stream != null) {
            final BufferedInputStream bis = new BufferedInputStream(stream);
            try {
                bundle = new XMLResourceBundle(bis);
            } finally {
                bis.close();
            }
        }
        return bundle;
    }
