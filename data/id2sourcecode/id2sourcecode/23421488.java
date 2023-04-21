    public HeartBeatFrag(EntityId readerId, EntityId writerId, SequenceNumber writerSN, FragmentNumber fn, Count count) {
        super(HEARTBEAT_FRAG.value);
        this.readerId = readerId;
        this.writerId = writerId;
        this.writerSN = writerSN;
        this.fn = fn;
        this.count = count;
    }
