    private static LayerReader[] loadResourceList(final String resourceName, ClassLoader loader) {
        if (loader == null) loader = ClassLoader.getSystemClassLoader();
        final Set result = new HashSet();
        try {
            final Enumeration resources = loader.getResources(resourceName);
            if (resources != null) {
                while (resources.hasMoreElements()) {
                    final URL url = (URL) resources.nextElement();
                    final Properties mapping;
                    InputStream urlIn = null;
                    try {
                        urlIn = url.openStream();
                        mapping = new Properties();
                        mapping.load(urlIn);
                    } catch (IOException ioe) {
                        continue;
                    } finally {
                        if (urlIn != null) try {
                            urlIn.close();
                        } catch (Exception ignore) {
                        }
                    }
                    for (Enumeration keys = mapping.propertyNames(); keys.hasMoreElements(); ) {
                        final String format = (String) keys.nextElement();
                        final String implClassName = mapping.getProperty(format);
                        result.add(loadResource(implClassName, loader));
                    }
                }
            }
        } catch (IOException ignore) {
        }
        return (LayerReader[]) result.toArray(new LayerReader[result.size()]);
    }
