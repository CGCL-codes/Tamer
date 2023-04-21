    public static String getChannelPrefix(String channelId) {
        if (!channelId.contains(CHANNELTOKENSTRING)) return null;
        return channelId.split(CHANNELTOKENSTRINGESCAPED)[0];
    }
