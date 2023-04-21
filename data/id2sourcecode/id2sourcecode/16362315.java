    public void removeChannel(Feed oldFeed) {
        if (hasNotAvailableCategory(oldFeed)) return;
        SortedMap<Feed, ChannelIF> channelMap = getChannelMap(oldFeed.getCategory());
        ChannelIF oldChannel = (ChannelIF) channelMap.get(oldFeed);
        channelMap.remove(oldFeed);
        cacheManager.removeChannelCache(oldChannel);
        pollingManager.unregisterChannel(oldChannel);
        if (logger.isInfoEnabled()) {
            logger.info("레포지토리에서 [" + oldFeed.getXmlUrl() + "] 채널 제거.");
        }
    }
