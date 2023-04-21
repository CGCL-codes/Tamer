    public static synchronized ChatChannel getChannel(String channelID) {
        if (channelID == null) {
            if (!CHANNELS.containsKey(null)) {
                return createChannel(null, null);
            }
        }
        return CHANNELS.get(channelID);
    }
