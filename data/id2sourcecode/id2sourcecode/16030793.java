    public static String getChannelSuffix(String channelId) {
        if (!channelId.contains(CHANNELTOKENSTRING)) return null;
        return channelId.split(CHANNELTOKENSTRINGESCAPED)[1];
    }
