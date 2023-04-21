    public void addChannel(Feed newFeed) {
        if (hasNotAvailableCategory(newFeed)) return;
        ChannelIF newChannel = channelManager.buildChannel(newFeed);
        if (newChannel == null) return;
        SortedMap<Feed, ChannelIF> channelMap = getChannelMap(newFeed.getCategory());
        channelMap.put(newFeed, newChannel);
        pollingManager.registerChannel(newChannel);
        if (logger.isInfoEnabled()) {
            logger.info("레포지토리에 [" + newFeed.getXmlUrl() + "] 채널 추가.");
        }
    }
