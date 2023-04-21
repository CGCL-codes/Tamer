    @Override
    public void relationshipRemoved(IRelationship relationship) {
        String channel = getChannelName(relationship);
        if (someoneIsListening(channel)) {
            publish(relationship.getSubordinate(), channel);
        }
    }
