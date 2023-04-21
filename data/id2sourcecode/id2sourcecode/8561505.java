    public DataFrag(EntityId readerId, EntityId writerId, SequenceNumber writerSN, KeyHashPrefix khp, KeyHashSuffix khs, ParameterList inlineQoS, FragmentNumber fsn, ShortWrapperSubmessageElement fis, ShortWrapperSubmessageElement fsize, LongWrapperSubmessageElement sampleSize, SerializedData serializedData) {
        super(DATA_FRAG.value);
        this.readerId = readerId;
        this.writerId = writerId;
        this.writerSN = writerSN;
        this.khp = khp;
        this.khs = khs;
        this.inlineQoS = inlineQoS;
        this.fsn = fsn;
        this.fis = fis;
        this.fsize = fsize;
        this.sampleSize = sampleSize;
        this.serializedData = serializedData;
        if (khp != null && khs != null) {
            super.setFlagAt(2, true);
        }
        if (inlineQoS != null) {
            super.setFlagAt(1, true);
        }
    }
