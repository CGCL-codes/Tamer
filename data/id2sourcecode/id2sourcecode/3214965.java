        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
            ResourceBundle bundle = null;
            if (baseName == null || locale == null || format == null || loader == null) throw new NullPointerException();
            if (format.equals("xml")) {
                String bundleName = this.toBundleName(baseName, locale);
                String resourceName = this.toResourceName(bundleName, format);
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
                    bundle = new XMLResourceBundle(bis);
                    bis.close();
                }
            }
            return bundle;
        }
