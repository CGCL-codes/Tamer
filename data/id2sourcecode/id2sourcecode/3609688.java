    private void init() {
        registerHooks();
        config = new ServerConfig();
        config.load(configFile);
        config.setRunning(true);
        LogManager.init();
        if (!config.getDataSources().isEmpty()) {
            log.info("Initializing the datasources...");
            for (DataSourceConfig datasource : config.getDataSources()) {
                DataSourceManager.getInstance().setDataSource(datasource, datasource.getName());
            }
        }
        if (config.getMailSessionConfig() != null) {
            log.info("Initializing the mail session...");
            MailSessionManager.getInstance().setConfiguration(config.getMailSessionConfig());
            MailSessionManager.getInstance().checkSession();
        }
        SystrayManager.open();
        channelManager = ChannelManager.getInstance();
        channelManager.clear();
        for (ChannelConfig cc : config.getChannels()) {
            cc.setPersistent(true);
            channelManager.createChannel(cc);
        }
        for (Listener listener : config.getListeners()) {
            if (listener.isAutoStart()) {
                listener.start();
            }
        }
        new ShutdownListener().start();
        for (Service service : config.getServices()) {
            if (service.isAutoStart()) {
                log.info("Starting service " + service.getName());
                service.start();
            }
        }
        VersionService.updateLatestVersion();
        if (VersionService.isNewVersionAvailable()) {
            log.warning("A new version is available (" + VersionService.getLatestVersion() + "), download it on http://jetrix.sf.net now!");
        }
        console = new ConsoleClient();
        new Thread(console).start();
        log.info("Server ready!");
    }
