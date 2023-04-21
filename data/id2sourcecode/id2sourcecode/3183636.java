    public void testSave() throws Exception {
        ServerConfig config = new ServerConfig();
        config.load(serverConfigURL);
        for (ChannelConfig cc : config.getChannels()) {
            cc.setPersistent(true);
            ChannelManager.getInstance().createChannel(cc, false);
        }
        config.save();
        ServerConfig config2 = new ServerConfig();
        config2.load(serverConfigURL);
    }
