    private NoKeyDataFrag decodeNoKeyDataFrag() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(_packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(_packet));
        SequenceNumber sn = new SequenceNumber(SequenceNumber_tHelper.read(_packet));
        ParameterList inlineQoS = null;
        if (BitUtility.getFlagAt(_flags, 1)) {
            inlineQoS = new ParameterList(_packet);
        }
        FragmentNumber fragmentStartingNum = new FragmentNumber(FragmentNumber_tHelper.read(_packet));
        short fragmentsInSubmessage = _packet.read_short();
        short fragmentSize = _packet.read_short();
        int sampleSize = _packet.read_long();
        SerializedData serializedData = new SerializedData(_packet, _nextSubmessageHeader - _packet.getCursorPosition());
        GlobalProperties.logger.log(Logger.INFO, CDRMessageProcessorDEBUG.class, "decodeNoKeyDataFrag()", "" + "Received NoKeyDataFrag submessage SN=" + sn.getLongValue());
        return new NoKeyDataFrag(readerId, writerId, sn, inlineQoS, fragmentStartingNum, fragmentsInSubmessage, fragmentSize, sampleSize, serializedData);
    }
