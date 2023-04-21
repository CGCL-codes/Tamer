    public MauMauCardFaceLoader(File rootDir, String faceName) {
        if (rootDir.getPath().endsWith(File.separator)) this.loaderRootDir = rootDir; else loaderRootDir = new File(rootDir.getPath() + File.separator);
        if (!loaderRootDir.exists() && !loaderRootDir.mkdirs()) {
            LOGGER.error(loaderRootDir.getPath() + " can not be created");
            return;
        }
        if (!loaderRootDir.isDirectory() || !loaderRootDir.canRead() || !loaderRootDir.canWrite()) {
            LOGGER.error(loaderRootDir.getPath() + " is not directory with read-write-access");
            return;
        }
        initDefaultDirectories(false);
        this.defaultFaceName = faceName.trim().toLowerCase();
        if (!synchronize()) {
            LOGGER.warn("Try to recreate defaults ...");
            initDefaultDirectories(true);
            if (!synchronize()) LOGGER.error("Failed to initialize " + this + " (" + ready() + ')');
        }
    }
