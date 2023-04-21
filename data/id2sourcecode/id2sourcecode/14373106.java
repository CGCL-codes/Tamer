    public HeartBeat(EntityId readerId, EntityId writerId, SequenceNumber firstSN, SequenceNumber lastSN, Count count) {
        super(HEARTBEAT.value);
        this.readerId = readerId;
        this.writerId = writerId;
        this.firstSN = firstSN;
        this.lastSN = lastSN;
        this.count = count;
    }
