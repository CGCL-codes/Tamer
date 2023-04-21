    public List<Filter> retrieveFiltersByCategories(Set<Category> filterCategories) {
        final Set<Key> channelKeys = getChannelKeys(filterCategories);
        return DataStoreHelper.retrieveAll(Filter.class, getQueryHandlerForChannels(channelKeys));
    }
