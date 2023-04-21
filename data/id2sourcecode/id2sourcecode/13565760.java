    private NoKeyData decodeNoKeyData() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(_packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(_packet));
        SequenceNumber sn = new SequenceNumber(SequenceNumber_tHelper.read(_packet));
        ParameterList inlineQoS = null;
        if (BitUtility.getFlagAt(_flags, 1)) {
            inlineQoS = new ParameterList(_packet);
        }
        SerializedData serializedData = null;
        if (BitUtility.getFlagAt(_flags, 2)) {
            serializedData = new SerializedData(_packet, _nextSubmessageHeader - _packet.getCursorPosition());
        }
        GlobalProperties.logger.log(Logger.INFO, CDRMessageProcessorDEBUG.class, "decodeNoKeyData()", "" + "Received NoKeyData submessage SN=" + sn.getLongValue());
        return new NoKeyData(readerId, writerId, sn, inlineQoS, serializedData);
    }
