    public DiskTileCache(String baseDirectory, String imageFormat) {
        m_baseDirectory = baseDirectory;
        m_imageFormat = imageFormat;
        if (hasAccessToBaseDirectory(baseDirectory)) {
            m_impl = new DiskTileCacheImpl();
            LOGGER.log(Level.INFO, "Caching tiles at '" + baseDirectory + "'");
        } else {
            m_impl = new NoOpTileCacheImpl();
            LOGGER.log(Level.SEVERE, "Unable to read and write directory '" + baseDirectory + "'");
        }
    }
