    private Data decodeData() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(_packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(_packet));
        SequenceNumber sn = new SequenceNumber(SequenceNumber_tHelper.read(_packet));
        KeyHashPrefix khp = null;
        KeyHashSuffix khs = null;
        byte[] bytes = null;
        if (BitUtility.getFlagAt(_flags, 3)) {
            khp = new KeyHashPrefix(KeyHashPrefix_tHelper.read(_packet));
            khs = new KeyHashSuffix(KeyHashSuffix_tHelper.read(_packet));
        }
        StatusInfo si = null;
        if (BitUtility.getFlagAt(_flags, 4)) {
            si = new StatusInfo(_packet.read_long());
        }
        ParameterList qos = null;
        if (BitUtility.getFlagAt(_flags, 1)) {
            qos = new ParameterList(_packet);
        }
        SerializedData serializedData = null;
        if (BitUtility.getFlagAt(_flags, 2)) {
            int dataLength = _nextSubmessageHeader - _packet.getCursorPosition();
            serializedData = new SerializedData(_packet, dataLength);
        }
        GlobalProperties.logger.log(Logger.INFO, CDRMessageProcessorDEBUG.class, "decodeData()", "" + "Decoded DATA submessage SN=" + sn.getLongValue() + " CONTENT=" + serializedData);
        return new Data(readerId, writerId, sn, khp, khs, si, qos, serializedData);
    }
