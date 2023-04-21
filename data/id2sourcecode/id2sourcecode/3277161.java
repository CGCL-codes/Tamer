        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
            if (baseName == null || locale == null || format == null || loader == null) throw new NullPointerException();
            ResourceBundle bundle = null;
            if (format.equals("utf8")) {
                String bundleName = toBundleName(baseName, locale);
                String resourceName = toResourceName(bundleName, "properties");
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
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(new BomStream(stream), "UTF8"));
                    bundle = new UTF8ResourceBundle(bReader);
                    bReader.close();
                }
            }
            return bundle;
        }
