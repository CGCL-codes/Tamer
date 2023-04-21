    public void removeCategory(Category oldCategory) {
        SortedMap<Feed, ChannelIF> channelMap = getChannelMap(oldCategory);
        if (channelMap != null) {
            cacheManager.removeChannelCaches(channelMap);
            pollingManager.unregisterChannels(channelMap);
            channelMap.clear();
        }
        categoryMap.remove(oldCategory);
        if (logger.isInfoEnabled()) {
            logger.info("레포지토리에서 [" + oldCategory.getName() + "] 카테고리 제거.");
        }
    }
