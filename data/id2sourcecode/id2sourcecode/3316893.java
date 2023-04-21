    private URL getURLFromJar(ClassLoader loader, URL url) throws IOException {
        JarInputStream jar = new JarInputStream(url.openStream());
        ZipEntry entry = jar.getNextEntry();
        while (entry != null) {
            String entryName = entry.getName();
            if (entryName.endsWith(".hs")) {
                URL[] urls = new URL[] { url };
                URLClassLoader urlLoader = new URLClassLoader(urls, loader);
                return HelpSet.findHelpSet(urlLoader, entryName);
            }
            entry = (ZipEntry) jar.getNextEntry();
        }
        throw new IOException("Help set jar file '" + url.getFile() + "' is missing help set (*.hs) file");
    }
