    private static GeomReader[] loadResourceList(final String resourceName, ClassLoader loader) {
        if (loader == null) loader = ClassLoader.getSystemClassLoader();
        final FastSet<GeomReader> result = FastSet.newInstance();
        try {
            final Enumeration<URL> resources = loader.getResources(resourceName);
            if (resources != null) {
                while (resources.hasMoreElements()) {
                    final URL url = resources.nextElement();
                    final Properties mapping;
                    InputStream urlIn = null;
                    try {
                        urlIn = url.openStream();
                        mapping = new Properties();
                        mapping.load(urlIn);
                    } catch (IOException ioe) {
                        continue;
                    } finally {
                        if (urlIn != null) urlIn.close();
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
        GeomReader[] resultArr = result.toArray(new GeomReader[result.size()]);
        Arrays.sort(resultArr, FastComparator.DEFAULT);
        FastSet.recycle(result);
        return resultArr;
    }
