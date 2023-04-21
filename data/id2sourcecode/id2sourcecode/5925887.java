    protected void export(ConfigEntry aConfigEntry, PluginInterface aPlugin) {
        final boolean doPeriodicUpdate = Boolean.valueOf(aConfigEntry.getProperty(AzureusConfigConstants.ENABLE_PERIODIC_UPDATE)).booleanValue();
        boolean isAsync = Boolean.valueOf(aConfigEntry.getProperty(AzureusConfigConstants.ASYNC_LOADING)).booleanValue();
        LoggerChannel logger = aPlugin.getLogger().getChannel("SafePeer");
        DefaultMiniLogger.createLoggerInstance(SafePeer.LOG_IDENTIFIER, aConfigEntry);
        MiniLogger log = DefaultMiniLogger.getInstance(SafePeer.LOG_IDENTIFIER);
        if (isAsync) {
            String logMsg = "Starting SafePeer Plugin asynchronously";
            logger.log(LoggerChannel.LT_INFORMATION, logMsg);
            log.info(logMsg);
        } else {
            String logMsg = "Starting SafePeer Plugin synchronously";
            logger.log(LoggerChannel.LT_INFORMATION, logMsg);
            log.info(logMsg);
        }
        long period = 0;
        if (doPeriodicUpdate) {
            try {
                String timer = aConfigEntry.getProperty(AzureusConfigConstants.UPDATE_TIMER);
                StringTokenizer tokenizer = new StringTokenizer(timer, ":");
                long hours = Long.valueOf(tokenizer.nextToken()).longValue();
                long minutes = Long.valueOf(tokenizer.nextToken()).longValue();
                long seconds = Long.valueOf(tokenizer.nextToken()).longValue();
                period = hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000;
                String logMsg = "Periodic update of the IpFilter will occur every " + hours + "h" + minutes + "m" + seconds + "s";
                logger.log(LoggerChannel.LT_INFORMATION, logMsg);
                log.info(logMsg);
            } catch (Exception ex) {
                period = 2 * 60 * 60 * 1000 + 0 * 60 * 1000 + 0 * 1000;
                String logMsg = "Periodic update of the IpFilter will occur every 2h";
                logger.log(LoggerChannel.LT_INFORMATION, logMsg);
                log.info(logMsg);
            }
        }
        if (isAsync) {
            final ConfigEntry entry = aConfigEntry;
            final long updatePeriod = period;
            final PluginInterface plugin = aPlugin;
            Thread t = new Thread() {

                public void run() {
                    setSafePeer(new SafePeer(entry));
                    exportToIpFilter(plugin);
                    if (doPeriodicUpdate) {
                        startExportTask(updatePeriod, plugin);
                    }
                }
            };
            t.setName("SafePeerAsyncLoader");
            t.setDaemon(true);
            t.start();
        } else {
            setSafePeer(new SafePeer(aConfigEntry));
            exportToIpFilter(aPlugin);
            if (doPeriodicUpdate) {
                startExportTask(period, aPlugin);
            }
        }
        startBlockLogTask(5 * 60 * 1000, aPlugin);
    }
