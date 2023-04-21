    public boolean addConfig(SensorMetaData smd, boolean overwrite) {
        if (smd == null) {
            throw new IllegalArgumentException("A null value is passed to add to " + "the SensorMetaData table");
        }
        if (hash.get(smd.getId()) == null || overwrite) {
            TurbineSrcConfig srcConfig = new TurbineSrcConfig(smd.getId());
            srcConfig.setChannelInfo(smd.getChannels(), smd.getChannelDatatypes());
            TurbineServer server = new TurbineServer();
            srcConfig.setServer(server);
            server.serverAddr = Constants.DEFAULT_SERVER_ADDRESS;
            server.userName = Constants.DEFAULT_SERVER_USERNAME;
            server.password = Constants.DEFAULT_SERVER_PASSWORD;
            hash.put(smd.getId(), srcConfig);
            return true;
        }
        return false;
    }
