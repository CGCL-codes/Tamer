    public CacheFileManagerImpl() {
        AEDiagnostics.addEvidenceGenerator(this);
        file_manager = FMFileManagerFactory.getSingleton();
        boolean enabled = COConfigurationManager.getBooleanParameter("diskmanager.perf.cache.enable");
        boolean enable_read = COConfigurationManager.getBooleanParameter("diskmanager.perf.cache.enable.read");
        boolean enable_write = COConfigurationManager.getBooleanParameter("diskmanager.perf.cache.enable.write");
        long size = 1024L * 1024L * COConfigurationManager.getIntParameter("diskmanager.perf.cache.size");
        int not_smaller_than = 1024 * COConfigurationManager.getIntParameter("notsmallerthan");
        if (size <= 0) {
            Debug.out("Invalid cache size parameter (" + size + "), caching disabled");
            enabled = false;
        }
        initialise(enabled, enable_read, enable_write, size, not_smaller_than);
    }
