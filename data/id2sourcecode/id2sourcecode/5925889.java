    protected void startExportTask(long aPeriod, PluginInterface aPlugin) {
        Timer timer = new Timer(true);
        final PluginInterface plugin = aPlugin;
        TimerTask task = new TimerTask() {

            public void run() {
                MiniLogger log = DefaultMiniLogger.getInstance(SafePeer.LOG_IDENTIFIER);
                LoggerChannel logger = plugin.getLogger().getChannel("SafePeer");
                String logMsg = "Performing periodic IpFilter update...";
                logger.log(LoggerChannel.LT_INFORMATION, logMsg);
                log.info(logMsg);
                boolean cacheUpdated = getSafePeer().updateCache();
                if (cacheUpdated) {
                    logMsg = "Ip ranges retrieved, performing update...";
                    logger.log(LoggerChannel.LT_INFORMATION, logMsg);
                    log.info(logMsg);
                    exportToIpFilter(plugin);
                    logMsg = "Periodic update performed";
                    logger.log(LoggerChannel.LT_INFORMATION, logMsg);
                    log.info(logMsg);
                } else {
                    logMsg = "Ip ranges of the database could not be updated, old ranges will be used";
                    logger.log(LoggerChannel.LT_WARNING, logMsg);
                    log.warn(logMsg);
                }
            }
        };
        timer.scheduleAtFixedRate(task, aPeriod, aPeriod);
    }
