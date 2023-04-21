    public Gap(EntityId readerId, EntityId writerId, SequenceNumber gapStart, SequenceNumberSet gapList) {
        super(GAP.value);
        this.readerId = readerId;
        this.writerId = writerId;
        this.gapStart = gapStart;
        this.gapList = gapList;
    }
