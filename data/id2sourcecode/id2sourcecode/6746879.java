    public static boolean subscribeChannels() throws BusException {
        boolean wasChannelBootupStarted = false;
        Context context = Registry.getActiveInstance().getContext();
        Configuration configuration = Configuration.getInstance(context);
        if (!configuration.isActive()) {
            return false;
        }
        Vector channels = AppConfig.getInstance().getChannels();
        boolean newAdded = false;
        if (channels != null && !channels.isEmpty()) {
            int size = channels.size();
            for (int i = 0; i < size; i++) {
                String channel = (String) channels.get(i);
                boolean cour = configuration.addMyChannel(channel);
                if (!newAdded && cour) {
                    newAdded = true;
                }
            }
            configuration.save(context);
            if (newAdded) {
                CometUtil.performChannelBootup(configuration);
                wasChannelBootupStarted = true;
            }
        }
        return wasChannelBootupStarted;
    }
