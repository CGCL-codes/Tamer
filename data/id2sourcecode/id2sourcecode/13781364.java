    public TiledRasterProducer(MemoryCache cache, int writeThreadPoolSize) {
        if (cache == null) {
            String message = Logging.getMessage("nullValue.CacheIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (writeThreadPoolSize < 1) {
            String message = Logging.getMessage("generic.ArgumentOutOfRange", "writeThreadPoolSize < 1");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        this.rasterCache = cache;
        this.tileWriteService = this.createDefaultTileWriteService(writeThreadPoolSize);
        this.tileWriteSemaphore = new java.util.concurrent.Semaphore(writeThreadPoolSize, true);
        try {
            readerFactory = (DataRasterReaderFactory) WorldWind.createConfigurationComponent(AVKey.DATA_RASTER_READER_FACTORY_CLASS_NAME);
        } catch (Exception e) {
            readerFactory = new BasicDataRasterReaderFactory();
        }
    }
