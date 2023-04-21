    public WebservicesChannel getWebservicesChannel(String channelId) {
        WebservicesChannel webservicesChannel = null;
        AssertUtility.notNull(channelId);
        Iterator<WebservicesChannel> iterator = webservicesChannels.iterator();
        while (iterator.hasNext()) {
            WebservicesChannel temp = iterator.next();
            if (temp.getChannelId().equalsIgnoreCase(channelId)) {
                webservicesChannel = temp;
            }
        }
        return webservicesChannel;
    }
