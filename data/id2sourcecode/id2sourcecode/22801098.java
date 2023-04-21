    public NackFrag(EntityId readerId, EntityId writerId, SequenceNumber writerSN, FragmentNumberSet fragmentNumberState, Count count) {
        super(NACK_FRAG.value);
        this.readerId = readerId;
        this.writerId = writerId;
        this.writerSN = writerSN;
        this.fragmentNumberState = fragmentNumberState;
        this.count = count;
    }
