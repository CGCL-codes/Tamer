    public List<Class<?>> getSubClasses() {
        List<Class<?>> returnValue = new ArrayList<Class<?>>();
        final Package[] packages = Package.getPackages();
        for (Package package1 : packages) {
            final String packageName = package1.getName();
            if (packageName.startsWith("sun.") || packageName.startsWith("javax.") || packageName.startsWith("java.")) {
                continue;
            }
            String pathName = getPathName(packageName);
            URL url = fieldClass.getResource(pathName);
            if (url == null) {
                LOGGER.debug("Could not find URL for " + pathName);
                continue;
            }
            String urlString = url.toString();
            if (urlString.startsWith("jar:")) {
                try {
                    addClasses(returnValue, (JarURLConnection) url.openConnection());
                } catch (IOException e) {
                    LOGGER.debug("Could not open jarfile", e);
                }
            } else if (urlString.startsWith("file:")) {
                addClasses(returnValue, packageName, new File(url.getFile()));
            }
        }
        return returnValue;
    }
