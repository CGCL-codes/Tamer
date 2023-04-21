    public synchronized PartitionerResponse partition(ReplDBMSHeader event, int taskId) throws ReplicatorException {
        if (shardTable == null) initialize();
        String shardId = event.getShardId();
        Integer partition = shardTable.get(shardId);
        if (partition == null) {
            if (defaultPartition >= 0) partition = new Integer(defaultPartition); else if (this.hashMethod == STRING_HASH) partition = new Integer(Math.abs(shardId.hashCode()) % availablePartitions); else if (hashMethod == ROUND_ROBIN) {
                if (shardTable.get(shardId) == null) {
                    Integer newPartition = channelAssignmentService.getChannelAssignment(shardId);
                    shardTable.put(shardId, newPartition);
                }
                partition = shardTable.get(shardId);
            }
        }
        boolean critical = (criticalShards.get(shardId) != null || ReplOptionParams.SHARD_ID_UNKNOWN.equals(shardId));
        return new PartitionerResponse(partition, critical);
    }
