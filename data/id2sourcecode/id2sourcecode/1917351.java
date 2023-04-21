    public ClassLoader addAllToClassPath(ClassLoader classLoader) {
        File directory = new File("plugins");
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (!directory.isDirectory()) {
            return classLoader;
        }
        File[] fileList = directory.listFiles();
        for (int j = 0; j < fileList.length; j++) {
            try {
                if (classLoader instanceof URLClassLoader) {
                    URL[] old = ((URLClassLoader) classLoader).getURLs();
                    URL[] new_urls = new URL[old.length + 1];
                    System.arraycopy(old, 0, new_urls, 1, old.length);
                    new_urls[0] = fileList[j].toURL();
                    return classLoader = new URLClassLoader(new_urls, classLoader);
                } else {
                    return classLoader = new URLClassLoader(new URL[] { fileList[j].toURL() }, classLoader);
                }
            } catch (Exception e) {
                return classLoader;
            }
        }
        return classLoader;
    }
