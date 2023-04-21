    public static String getChannelVar(String channelName, int channel) {
        return GA_VAR.format(new Object[] { channelName, new Integer(channel) });
    }
