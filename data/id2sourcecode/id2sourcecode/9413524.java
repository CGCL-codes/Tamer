    private List<String> findTwoWaySyncChannels() {
        List<String> channelsToSync = new ArrayList<String>();
        AppConfig appConfig = AppConfig.getInstance();
        Vector appChannels = appConfig.getChannels();
        if (appChannels != null) {
            int size = appChannels.size();
            for (int i = 0; i < size; i++) {
                String channel = (String) appChannels.get(i);
                if (MobileBean.isBooted(channel)) {
                    channelsToSync.add(channel);
                }
            }
        }
        return channelsToSync;
    }
