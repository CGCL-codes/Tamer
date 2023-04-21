    public String getChannelName() {
        if (Constants.CHANNEL_CODE_MAP.containsKey(channelCode)) {
            channelName = Constants.CHANNEL_CODE_MAP.get(channelCode);
        }
        return channelName;
    }
