    private DiskWriterManager() {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, " \n\n --------> DiskWriterManager is instantiating <--------------- \n\n");
        }
        MAX_PARTITION_COUNT = config.getMaxPartitionCount();
        writersPerPartionCount = config.getWritersCount();
        if (writersPerPartionCount < 0) {
            writersPerPartionCount = 1;
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "DiskWriterManager will use: " + writersPerPartionCount + " writers per partition");
        }
        execService = Utils.getStandardExecService("DiskWriterTask ", 1, MAX_PARTITION_COUNT * writersPerPartionCount, Thread.NORM_PRIORITY);
        ScheduledExecutorService monitoringService = Utils.getMonitoringExecService();
        monitoringService.scheduleWithFixedDelay(new DiskWriterManagerMonitoringTask(this), 1, 5, TimeUnit.SECONDS);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, " \n\n --------> DiskWriterManager is instantiatied <--------------- \n\n");
        }
    }
