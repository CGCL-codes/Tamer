    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
        if (baseName == null) {
            throw new NullPointerException("baseName is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is null");
        }
        if (format == null) {
            throw new NullPointerException("format is null");
        }
        if (!extension.equals(format)) {
            throw new IllegalArgumentException("unsupported format: " + format);
        }
        if (loader == null) {
            throw new NullPointerException("loader is null");
        }
        String resourceName = toResourceName(toBundleName(baseName, locale), format);
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
        if (stream == null) {
            return null;
        }
        try {
            return readBundle(stream);
        } finally {
            try {
                stream.close();
            } catch (IOException ignored) {
            }
        }
    }
