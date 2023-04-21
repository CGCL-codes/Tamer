    @Override
    protected void configure() {
        LogManager logManager = LogManager.getLogManager();
        try {
            URL url = LoggingUtil.searchLoggingFile();
            logManager.readConfiguration(url.openStream());
            logger.config("Config logging use " + url);
        } catch (Exception e) {
            System.err.println("TestingModule: Load logging configuration failed");
            System.err.println("" + e);
        }
        bind(GuiceBerryEnvMain.class).to(PersistServiceStarter.class);
    }
