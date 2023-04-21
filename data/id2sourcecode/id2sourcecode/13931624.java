    @Override
    public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
        String resource = this.toResourceName(baseName, format).replaceAll("java\\.properties", "properties");
        URL url = loader.getResource(resource);
        boolean result = false;
        try {
            if (url != null) {
                long lastModified = 0;
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    if (connection instanceof JarURLConnection) {
                        JarEntry ent = ((JarURLConnection) connection).getJarEntry();
                        if (ent != null) {
                            lastModified = ent.getTime();
                            if (lastModified == -1) {
                                lastModified = 0;
                            }
                        }
                    } else {
                        lastModified = connection.getLastModified();
                    }
                }
                result = lastModified >= loadTime;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
