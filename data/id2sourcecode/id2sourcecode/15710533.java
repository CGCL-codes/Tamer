    private synchronized void initialize() throws InitializationException, Exception {
        final Server server = opc.getServer();
        server.setDefaultUpdateRate(samplingRate);
        final Group group = server.addGroup();
        final int actualSampling = group.getUpdateRate();
        if (actualSampling != samplingRate) {
            logger.log(Level.WARNING, "The OPC server update rate was changed to {0} milliseconds by the server. Logging/sampling will continue at the requested rate but the server will have a different update rate.", actualSampling);
        }
        final OPCItemInformation preparedInfo = prepareOPCItems(group);
        prepareOPCDataProvider(group);
        writer.initialize(preparedInfo.itemInfo, selection.composite.toArray(new CompositeItem[selection.composite.size()]), readProperties);
    }
