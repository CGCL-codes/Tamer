    public static Module getJarFileFromClassLoader(String fileName, ClassLoader loader) throws IOException {
        if (fileName == null) {
            throw new IllegalArgumentException("null fileName");
        }
        if (loader == null) {
            throw new IllegalArgumentException("null loader");
        }
        URL url = loader.getResource(fileName);
        if (DEBUG_LEVEL > 0) {
            System.err.println("FileProvider got url: " + url + " for " + fileName);
        }
        if (url == null) {
            try {
                return new JarFileModule(new JarFile(fileName, false));
            } catch (ZipException e) {
                throw new IOException("Could not find file: " + fileName);
            }
        }
        if (url.getProtocol().equals("jar")) {
            JarURLConnection jc = (JarURLConnection) url.openConnection();
            JarFile f = jc.getJarFile();
            JarEntry entry = jc.getJarEntry();
            JarFileModule parent = new JarFileModule(f);
            return new NestedJarFileModule(parent, entry);
        } else {
            String filePath = filePathFromURL(url);
            return new JarFileModule(new JarFile(filePath, false));
        }
    }
