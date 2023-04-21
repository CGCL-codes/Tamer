    @Override
    public void relationshipRemoved(IRelationship relationship) {
        String channel = getChannelName(relationship);
        ChannelCacheController.getChannelCache().remove(channel);
    }
