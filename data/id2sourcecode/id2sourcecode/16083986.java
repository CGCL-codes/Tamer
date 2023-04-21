    private void init(String cacheDirectoryPath, int cachesize, int cleansize, int aperturesize) {
        diskcache_ = new DiskCache(cacheDirectoryPath);
        memorycache_ = new MemoryCache(cachesize, cleansize, aperturesize);
        writecacherunnable_ = new WriteCacheRunnable();
        final Thread writecacherunnablethread = new Thread(writecacherunnable_);
        writecacherunnablethread.setName("CacheWriterThread");
        writecacherunnablethread.start();
    }
