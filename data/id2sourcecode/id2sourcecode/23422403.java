    public Archive createArchive(URL url) throws URISyntaxException, IOException {
        logger.entering("ArchiveFactoryImpl", "createArchive", new Object[] { url });
        Archive result;
        String protocol = url.getProtocol();
        logger.logp(Level.FINER, "ArchiveFactoryImpl", "createArchive", "protocol = {0}", protocol);
        if ("file".equals(protocol)) {
            URI uri = null;
            try {
                uri = url.toURI();
            } catch (URISyntaxException exception) {
                uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
            }
            File f = new File(uri);
            if (f.isDirectory()) {
                result = new DirectoryArchive(f);
            } else {
                result = new JarFileArchive(new JarFile(f));
            }
        } else if ("jar".equals(protocol)) {
            JarURLConnection conn = JarURLConnection.class.cast(url.openConnection());
            JarEntry je = conn.getJarEntry();
            if (je == null) {
                result = new JarFileArchive(conn.getJarFile());
            } else if (je.isDirectory()) {
                result = new DirectoryInsideJarURLArchive(url);
            } else {
                result = new JarInputStreamURLArchive(url);
            }
        } else if (isJarInputStream(url)) {
            result = new JarInputStreamURLArchive(url);
        } else {
            result = new URLArchive(url);
        }
        logger.exiting("ArchiveFactoryImpl", "createArchive", result);
        return result;
    }
