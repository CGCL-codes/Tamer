    private void createCOOS(InputStream is) throws Exception {
        COOS coos = null;
        try {
            coos = COOSFactory.createCOOS(is, this);
            coos.start();
        } catch (Exception e) {
            LOG.info("Failed to load COOS(s)", e);
            throw e;
        }
        COOSs.add(coos);
        ChannelServer channelServer = coos.getChannelServer("default");
        context.registerService(ChannelServer.class.getName(), channelServer, new Hashtable());
        LOG.info("ChannelServer registered");
    }
