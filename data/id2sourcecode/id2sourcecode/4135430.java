        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            if ((baseName == null) || (locale == null) || (format == null) || (loader == null)) throw new NullPointerException();
            if (!XML.contains(format)) return null;
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, format);
            URL url = loader.getResource(resourceName);
            if (url == null) return null;
            URLConnection c = url.openConnection();
            if (c == null) return null;
            if (reload) c.setUseCaches(false);
            InputStream bis = new BufferedInputStream(c.getInputStream());
            try {
                return new XMLResourceBundle(bis);
            } catch (Exception e) {
                throw new IOException(e);
            } finally {
                bis.close();
            }
        }
