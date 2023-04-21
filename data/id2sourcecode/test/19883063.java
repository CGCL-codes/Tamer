    public List<Blip> retrieveBlipsByCategories(Set<Category> blipCategories) {
        final Set<Key> channelKeys = getChannelKeys(blipCategories);
        return DataStoreHelper.retrieveAll(Blip.class, getQueryHandlerForChannels(channelKeys));
    }
