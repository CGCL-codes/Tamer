    public static Module getJarFileFromClassLoader(String fileName) throws IOException {
        URL url = FileProvider.class.getClassLoader().getResource(fileName);
        if (DEBUG_LEVEL > 0) {
            Trace.println("FileProvider got url: " + url + " for " + fileName);
        }
        if (url == null) {
            return new JarFileModule(new JarFile(fileName, false));
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
