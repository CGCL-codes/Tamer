    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        if (format.equals("java.properties")) {
            String bundleName = toBundleName(baseName, locale);
            ResourceBundle bundle = null;
            final String resourceName = toResourceName(bundleName, "properties");
            final ClassLoader classLoader = loader;
            final boolean reloadFlag = reload;
            InputStream stream = null;
            try {
                stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {

                    public InputStream run() throws IOException {
                        InputStream is = null;
                        if (reloadFlag) {
                            URL url = classLoader.getResource(resourceName);
                            if (url != null) {
                                URLConnection connection = url.openConnection();
                                if (connection != null) {
                                    connection.setUseCaches(false);
                                    is = connection.getInputStream();
                                }
                            }
                        } else {
                            is = classLoader.getResourceAsStream(resourceName);
                        }
                        return is;
                    }
                });
            } catch (PrivilegedActionException e) {
                throw (IOException) e.getException();
            }
            if (stream != null) {
                try {
                    Reader reader = new InputStreamReader(stream, Charset.forName(charsetName));
                    bundle = new PropertyResourceBundle(reader);
                } finally {
                    stream.close();
                }
            }
            return bundle;
        } else {
            return super.newBundle(baseName, locale, format, loader, reload);
        }
    }
