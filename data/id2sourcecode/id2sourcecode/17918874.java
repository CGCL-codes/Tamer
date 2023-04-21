    public NoKeyData(EntityId readerId, EntityId writerId, SequenceNumber writerSN, ParameterList inlineQoS, SerializedData serializedData) {
        super(NOKEY_DATA.value);
        this.readerId = readerId;
        this.writerId = writerId;
        this.writerSN = writerSN;
        this.inlineQoS = inlineQoS;
        this.serializedData = serializedData;
        if (inlineQoS != null) {
            super.setFlagAt(1, true);
        }
        if (serializedData != null) {
            super.setFlagAt(2, true);
        }
    }
