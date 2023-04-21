    @Override
    public void relationshipRemoved(IRelationship relationship) {
        String channel = Channels.getAddRelationshipChannel(relationship);
        ChannelCacheController.getChannelCache().remove(channel);
        channel = Channels.getAddRelationshipWildcardChannel(relationship);
        ChannelCacheController.getChannelCache().remove(channel);
    }
