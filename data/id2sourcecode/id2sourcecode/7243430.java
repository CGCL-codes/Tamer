    public void startSystemChannels() throws ChannelLifeCycleException {
        User lRoot = SecurityFactory.getRootUser();
        JWebSocketConfig lConfig = JWebSocketConfig.getConfig();
        for (ChannelConfig lCfg : lConfig.getChannels()) {
            if (lCfg.isSystemChannel()) {
                lCfg.validate();
                if (LOGGER_CHANNEL_ID.equals(lCfg.getId())) {
                    mLoggerChannel = new Channel(lCfg);
                    mLoggerChannel.start(lRoot.getLoginname());
                } else if (ADMIN_CHANNEL_ID.equals(lCfg.getId())) {
                    mAdminChannel = new Channel(lCfg);
                    mAdminChannel.start(lRoot.getLoginname());
                } else {
                    Channel lChannel = new Channel(lCfg);
                    lChannel.start(lRoot.getLoginname());
                    mSystemChannels.put(lChannel.getId(), lChannel);
                }
            }
        }
    }
