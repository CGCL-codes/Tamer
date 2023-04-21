    public AckNack(EntityId readerId, EntityId writerId, SequenceNumberSet sns, Count count) {
        super(ACKNACK.value);
        this.readerId = readerId;
        this.writerId = writerId;
        this.sns = sns;
        this.count = count;
    }
