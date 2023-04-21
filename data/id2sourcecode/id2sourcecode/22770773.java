    public PropertyFileUrlMap(final Properties config) throws IOException {
        syslog = Logger.getLogger(config.getProperty(App.CFG_LOG_SYSTEM));
        writeThreadCheckInterval = config.getProperty(CFG_WRITE_INTERVAL) == null ? DEFAULT_WRITE_CHECK_INTERVAL : Long.valueOf(config.getProperty(CFG_WRITE_INTERVAL)) * 1000L;
        backingFile = new File(config.getProperty(CFG_FILEPATH));
        final boolean isNewBackingStore = backingFile.createNewFile();
        final FileReader in = new FileReader(backingFile);
        try {
            urlMap = new Properties();
            urlMap.load(in);
        } finally {
            in.close();
        }
        shortener = new BrokenSoManyWaysShortenerStrategy(this);
        writeThread = new Thread(new WriteDaemon());
        writeThread.setName("WriterThread");
        writeThread.setPriority(Math.max(Thread.currentThread().getPriority() - 1, Thread.MIN_PRIORITY));
        writeThread.setDaemon(true);
        keepRunning = true;
        writeThread.start();
    }
