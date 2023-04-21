    private void autoConfigDatabase(boolean force) {
        if (parameterService.is(ParameterConstants.AUTO_CONFIGURE_DATABASE) || force) {
            logger.info("Initializing SymmetricDS database.");
            dbDialect.initSupportDb();
            if (defaultChannels != null) {
                configurationService.flushChannels();
                List<NodeChannel> channels = configurationService.getChannels();
                for (Channel defaultChannel : defaultChannels) {
                    if (!defaultChannel.isInList(channels)) {
                        logger.info(String.format("Auto configuring %s channel", defaultChannel.getId()));
                        configurationService.saveChannel(defaultChannel);
                    } else {
                        logger.info(String.format("No need to create channel %s.  It already exists", defaultChannel.getId()));
                    }
                }
                configurationService.flushChannels();
            }
            parameterService.rereadParameters();
            logger.info("Done initializing SymmetricDS database.");
        } else {
            logger.info("SymmetricDS is not configured to auto create the database.");
        }
    }
