    @Override
    public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(baseName, locale, format, loader, reload));
        final String bundleName = toBundleName(baseName, locale);
        final String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle result = null;
        InputStream stream = null;
        if (reload) {
            final URL url = loader.getResource(resourceName);
            if (null != url) {
                final URLConnection connection = url.openConnection();
                if (null != connection) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (null != stream) {
            try {
                result = new PropertyResourceBundle(new InputStreamReader(stream, encoding));
            } finally {
                stream.close();
            }
        }
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit(result));
        return result;
    }
