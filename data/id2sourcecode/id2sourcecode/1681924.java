    public URLConnection openConnection(URL url) throws IOException {
        if (expectedUrl == null || !expectedUrl.equals(url)) {
            if (!url.getProtocol().equals("jar")) {
                throw new IllegalArgumentException("Unsupported protocol " + url.getProtocol());
            }
            String path = url.getPath();
            String[] chunks = path.split("!/", 2);
            if (chunks.length == 1) {
                throw new MalformedURLException("Url does not contain a '!' character: " + url);
            }
            String file = chunks[0];
            String entryPath = chunks[1];
            if (!file.startsWith("file:")) {
                return new URL(url.toExternalForm()).openConnection();
            }
            file = file.substring("file:".length());
            File f = new File(file);
            if (f.exists()) {
                jarFile = new JarFile(f);
            }
            if (jarFile == null) {
                throw new FileNotFoundException("Cannot find JarFile: " + file);
            }
            jarEntry = jarFile.getJarEntry(entryPath);
            if (jarEntry == null) {
                throw new FileNotFoundException("Entry not found: " + url);
            }
            expectedUrl = url;
        }
        return new JarFileUrlConnection(url, jarFile, jarEntry);
    }
