    private boolean isMyChannel(String channel) {
        Vector myChannels = AppConfig.getInstance().getChannels();
        if (myChannels != null && !myChannels.isEmpty()) {
            return myChannels.contains(channel);
        }
        return false;
    }
