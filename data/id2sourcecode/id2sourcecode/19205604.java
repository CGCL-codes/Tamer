    public synchronized Integer getChannelAssignment(String shardId) throws ReplicatorException {
        Integer channel = assignments.get(shardId);
        if (channel == null) {
            if (nextChannel >= channels) {
                nextChannel = 0;
            }
            channel = nextChannel++;
            insertChannelAssignment(shardId, channel);
        }
        return channel;
    }
