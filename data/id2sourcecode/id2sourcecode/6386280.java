    @Override
    public void relationshipAdded(IRelationship newRelationship) {
        IContribution subordinate = newRelationship.getSubordinate();
        if (subordinate != null && subordinate.getRelationships() != null && !subordinate.getRelationships().isEmpty()) {
            for (IRelationship relationshipFromSuperior : subordinate.getRelationships()) {
                String channel = Channels.getAddRelationshipChannel(relationshipFromSuperior);
                ChannelCacheController.getChannelCache().remove(channel);
                channel = Channels.getAddRelationshipWildcardChannel(relationshipFromSuperior);
                ChannelCacheController.getChannelCache().remove(channel);
            }
        }
    }
