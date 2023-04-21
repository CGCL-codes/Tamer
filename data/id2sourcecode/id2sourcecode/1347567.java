    public List<String> getChannels() throws Exception {
        String channels = SageApi.StringApi("GetFavoriteChannel", new Object[] { sageFavorite });
        String[] channelArray = channels.split("[;,]");
        List<String> channelList = new ArrayList<String>();
        for (String channel : channelArray) {
            if ((channel != null) && (channel.trim().length() > 0)) {
                channelList.add(channel);
            }
        }
        return channelList;
    }
