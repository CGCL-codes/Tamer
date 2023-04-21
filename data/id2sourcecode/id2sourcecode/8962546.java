    public Data(EntityId readerId, EntityId writerId, SequenceNumber writerSN, KeyHashPrefix khp, KeyHashSuffix khs, StatusInfo si, ParameterList inlineQoS, SerializedData serializedData) {
        super(DATA.value);
        this.readerId = readerId;
        this.writerId = writerId;
        this.writerSN = writerSN;
        this.khp = khp;
        this.khs = khs;
        this.si = si;
        this.inlineQoS = inlineQoS;
        this.serializedData = serializedData;
        if (khp != null && khs != null) {
            super.setFlagAt(3, true);
        }
        if (si != null) {
            super.setFlagAt(4, true);
        }
        if (inlineQoS != null) {
            super.setFlagAt(1, true);
        }
        if (serializedData != null) {
            super.setFlagAt(2, true);
        }
    }
