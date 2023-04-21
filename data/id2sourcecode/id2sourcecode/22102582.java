    private Manifest expand(URL url, StringBuffer buffer) throws RepositoryException {
        try {
            URL jurl = new URL("jar:" + url.toString() + "!/");
            JarURLConnection connection = (JarURLConnection) jurl.openConnection();
            Manifest manifest = connection.getManifest();
            final String group = getBlockGroup(manifest);
            buffer.append("\nBlock Group: " + group);
            final File root = new File(m_cache, group);
            buffer.append("\nLocal target: " + root);
            JarFile jar = connection.getJarFile();
            Enumeration entries = jar.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (!entry.getName().startsWith("META-INF")) {
                    installEntry(buffer, root, jar, entry);
                }
            }
            buffer.append("\nInstall successful.");
            return manifest;
        } catch (Throwable e) {
            final String error = "Could not install block: " + url;
            throw new RepositoryException(error, e);
        }
    }
