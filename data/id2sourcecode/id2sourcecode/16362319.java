    public void updateCategory(Category oldCategory, Category newCategory) {
        SortedMap<Feed, ChannelIF> channelMap = getChannelMap(oldCategory);
        categoryMap.remove(oldCategory);
        categoryMap.put(newCategory, channelMap);
        if (logger.isInfoEnabled()) {
            logger.info("레포지토리에서 [" + newCategory.getName() + "(ID: " + newCategory.getId() + ")] 카테고리 업데이트.");
        }
    }
