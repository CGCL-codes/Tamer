    public int getChannelType(int xActor, int yActor) {
        if (xActor >= numberOfXActors || yActor >= numberOfYActors) return CHANNEL_ERROR;
        return channels[xActor][yActor];
    }
