    private DataFrag decodeDataFrag() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(_packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(_packet));
        SequenceNumber sn = new SequenceNumber(SequenceNumber_tHelper.read(_packet));
        KeyHashPrefix khp = null;
        KeyHashSuffix khs = null;
        if (BitUtility.getFlagAt(_flags, 2)) {
            khp = new KeyHashPrefix(KeyHashPrefix_tHelper.read(_packet));
            khs = new KeyHashSuffix(KeyHashSuffix_tHelper.read(_packet));
        }
        ParameterList qos = null;
        if (BitUtility.getFlagAt(_flags, 1)) {
            qos = new ParameterList(_packet);
        }
        FragmentNumber fsn = new FragmentNumber(FragmentNumber_tHelper.read(_packet));
        ShortWrapperSubmessageElement fis = new ShortWrapperSubmessageElement(_packet.read_short());
        ShortWrapperSubmessageElement fsize = new ShortWrapperSubmessageElement(_packet.read_short());
        LongWrapperSubmessageElement sampleSize = new LongWrapperSubmessageElement(_packet.read_long());
        SerializedData serializedData = new SerializedData(_packet, _nextSubmessageHeader - _packet.getCursorPosition());
        GlobalProperties.logger.log(Logger.INFO, CDRMessageProcessorDEBUG.class, "decodeDataFrag()", "" + "Received DataFrag submessage SN=" + sn.getLongValue());
        return new DataFrag(readerId, writerId, sn, khp, khs, qos, fsn, fis, fsize, sampleSize, serializedData);
    }
