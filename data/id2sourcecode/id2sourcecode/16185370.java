    private NoKeyData decodeNoKeyData() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(packet));
        SequenceNumber sn = new SequenceNumber(SequenceNumber_tHelper.read(packet));
        ParameterList inlineQoS = null;
        if (BitUtility.getFlagAt(flags, 1)) {
            inlineQoS = new ParameterList(packet);
        }
        SerializedData serializedData = null;
        if (BitUtility.getFlagAt(flags, 2)) {
            serializedData = new SerializedData(packet, nextSubmessageHeader - packet.getCursorPosition());
        }
        logInfo("decodeNoKeyData()", "" + "Received NoKeyData submessage SN=" + sn.getLongValue());
        return new NoKeyData(readerId, writerId, sn, inlineQoS, serializedData);
    }
