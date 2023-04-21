    private Data decodeData() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(packet));
        SequenceNumber sn = new SequenceNumber(SequenceNumber_tHelper.read(packet));
        KeyHashPrefix khp = null;
        KeyHashSuffix khs = null;
        byte[] bytes = null;
        if (BitUtility.getFlagAt(flags, 3)) {
            khp = new KeyHashPrefix(KeyHashPrefix_tHelper.read(packet));
            khs = new KeyHashSuffix(KeyHashSuffix_tHelper.read(packet));
        }
        StatusInfo si = null;
        if (BitUtility.getFlagAt(flags, 4)) {
            si = new StatusInfo(packet.read_long());
        }
        ParameterList qos = null;
        if (BitUtility.getFlagAt(flags, 1)) {
            qos = new ParameterList(packet);
        }
        SerializedData serializedData = null;
        if (BitUtility.getFlagAt(flags, 2)) {
            int dataLength = nextSubmessageHeader - packet.getCursorPosition();
            serializedData = new SerializedData(packet, dataLength);
        }
        logInfo("decodeData()", "" + "Decoded DATA submessage SN=" + sn.getLongValue() + " CONTENT=" + serializedData);
        return new Data(readerId, writerId, sn, khp, khs, si, qos, serializedData);
    }
