    public FileManager(EnvironmentImpl envImpl, File dbEnvHome, boolean readOnly) throws EnvironmentLockedException {
        this.envImpl = envImpl;
        this.dbEnvHome = dbEnvHome;
        this.readOnly = readOnly;
        boolean success = false;
        stats = new StatGroup(LogStatDefinition.FILEMGR_GROUP_NAME, LogStatDefinition.FILEMGR_GROUP_DESC);
        nRandomReads = new LongStat(stats, FILEMGR_RANDOM_READS);
        nRandomWrites = new LongStat(stats, FILEMGR_RANDOM_WRITES);
        nSequentialReads = new LongStat(stats, FILEMGR_SEQUENTIAL_READS);
        nSequentialWrites = new LongStat(stats, FILEMGR_SEQUENTIAL_WRITES);
        nRandomReadBytes = new LongStat(stats, FILEMGR_RANDOM_READ_BYTES);
        nRandomWriteBytes = new LongStat(stats, FILEMGR_RANDOM_WRITE_BYTES);
        nSequentialReadBytes = new LongStat(stats, FILEMGR_SEQUENTIAL_READ_BYTES);
        nSequentialWriteBytes = new LongStat(stats, FILEMGR_SEQUENTIAL_WRITE_BYTES);
        nFileOpens = new IntStat(stats, FILEMGR_FILE_OPENS);
        nOpenFiles = new IntStat(stats, FILEMGR_OPEN_FILES);
        nBytesReadFromWriteQueue = new LongStat(stats, FILEMGR_BYTES_READ_FROM_WRITEQUEUE);
        nBytesWrittenFromWriteQueue = new LongStat(stats, FILEMGR_BYTES_WRITTEN_FROM_WRITEQUEUE);
        nReadsFromWriteQueue = new LongStat(stats, FILEMGR_READS_FROM_WRITEQUEUE);
        nWritesFromWriteQueue = new LongStat(stats, FILEMGR_WRITES_FROM_WRITEQUEUE);
        nWriteQueueOverflow = new LongStat(stats, FILEMGR_WRITEQUEUE_OVERFLOW);
        nWriteQueueOverflowFailures = new LongStat(stats, FILEMGR_WRITEQUEUE_OVERFLOW_FAILURES);
        nLogFSyncs = new LongStat(stats, FILEMGR_LOG_FSYNCS);
        try {
            DbConfigManager configManager = envImpl.getConfigManager();
            maxFileSize = configManager.getLong(EnvironmentParams.LOG_FILE_MAX);
            useWriteQueue = configManager.getBoolean(EnvironmentParams.LOG_USE_WRITE_QUEUE);
            writeQueueSize = configManager.getInt(EnvironmentParams.LOG_WRITE_QUEUE_SIZE);
            useODSYNC = configManager.getBoolean(EnvironmentParams.LOG_USE_ODSYNC);
            VERIFY_CHECKSUMS = configManager.getBoolean(EnvironmentParams.LOG_VERIFY_CHECKSUMS);
            if (!envImpl.isMemOnly()) {
                if (!dbEnvHome.exists()) {
                    throw new IllegalArgumentException("Environment home " + dbEnvHome + " doesn't exist");
                }
                if (!lockEnvironment(readOnly, false)) {
                    throw new EnvironmentLockedException(envImpl, "The environment cannot be locked for " + (readOnly ? "shared" : "single writer") + " access.");
                }
            }
            fileCache = new FileCache(configManager);
            fileCacheLatch = new Latch(DEBUG_NAME + "_fileCache");
            currentFileNum = 0L;
            nextAvailableLsn = DbLsn.makeLsn(currentFileNum, firstLogEntryOffset());
            lastUsedLsn = DbLsn.NULL_LSN;
            perFileLastUsedLsn = new HashMap<Long, Long>();
            prevOffset = 0L;
            endOfLog = new LogEndFileDescriptor();
            forceNewFile = false;
            saveLastPosition();
            final String stopOnWriteCountName = "je.debug.stopOnWriteCount";
            final String stopOnWriteCountProp = System.getProperty(stopOnWriteCountName);
            if (stopOnWriteCountProp != null) {
                try {
                    STOP_ON_WRITE_COUNT = Long.parseLong(stopOnWriteCountProp);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Could not parse: " + stopOnWriteCountName, e);
                }
            }
            final String stopOnWriteActionName = "je.debug.stopOnWriteAction";
            final String stopOnWriteActionProp = System.getProperty(stopOnWriteActionName);
            if (stopOnWriteActionProp != null) {
                if (stopOnWriteActionProp.compareToIgnoreCase("throw") == 0) {
                    THROW_ON_WRITE = true;
                } else if (stopOnWriteActionProp.compareToIgnoreCase("stop") == 0) {
                    THROW_ON_WRITE = false;
                } else {
                    throw new IllegalArgumentException("Unknown value for: " + stopOnWriteActionName + stopOnWriteActionProp);
                }
            }
            syncManager = new FSyncManager(envImpl);
            success = true;
        } finally {
            if (!success) {
                try {
                    close();
                } catch (IOException e) {
                }
            }
        }
    }
