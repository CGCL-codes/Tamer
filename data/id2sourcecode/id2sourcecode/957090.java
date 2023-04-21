    public NoKeyDataFrag(EntityId readerId, EntityId writerId, SequenceNumber writerSN, ParameterList inlineQoS, FragmentNumber fragmentStartingNum, short fragmentsInSubmessage, short fragmentSize, int sampleSize, SerializedData serializedData) {
        super(NOKEY_DATA_FRAG.value);
        this.readerId = readerId;
        this.writerId = writerId;
        this.writerSN = writerSN;
        this.inlineQoS = inlineQoS;
        this.fragmentStartingNum = fragmentStartingNum;
        this.fragmentsInSubmessage = fragmentsInSubmessage;
        this.fragmentSize = fragmentSize;
        this.sampleSize = sampleSize;
        this.serializedData = serializedData;
        if (inlineQoS != null) {
            super.setFlagAt(1, true);
        }
    }
