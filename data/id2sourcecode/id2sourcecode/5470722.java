    public DirectoryInsideJarURLArchive(URL url, Logger logger) throws IOException {
        logger.entering("DirectoryInsideJarURLArchive", "DirectoryInsideJarURLArchive", new Object[] { url });
        this.logger = logger;
        assert (url.getProtocol().equals("jar"));
        rootURL = url;
        JarURLConnection conn = JarURLConnection.class.cast(url.openConnection());
        jarFile = conn.getJarFile();
        logger.logp(Level.FINER, "DirectoryInsideJarURLArchive", "DirectoryInsideJarURLArchive", "jarFile = {0}", jarFile);
        relativeRootPath = conn.getEntryName();
        init();
    }
