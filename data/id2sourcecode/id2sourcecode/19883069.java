    private Set<Key> getChannelKeys(Set<Category> blipCategories) {
        List<Channel> channels = retrieveChannelsByCategories(blipCategories);
        final Set<Key> channelKeys = new HashSet<Key>();
        for (Channel channel : channels) {
            channelKeys.add(channel.getKey());
        }
        return channelKeys;
    }
