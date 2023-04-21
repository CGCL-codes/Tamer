    private DataFrag decodeDataFrag() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(packet));
        SequenceNumber sn = new SequenceNumber(SequenceNumber_tHelper.read(packet));
        KeyHashPrefix khp = null;
        KeyHashSuffix khs = null;
        if (BitUtility.getFlagAt(flags, 2)) {
            khp = new KeyHashPrefix(KeyHashPrefix_tHelper.read(packet));
            khs = new KeyHashSuffix(KeyHashSuffix_tHelper.read(packet));
        }
        ParameterList qos = null;
        if (BitUtility.getFlagAt(flags, 1)) {
            qos = new ParameterList(packet);
        }
        FragmentNumber fsn = new FragmentNumber(FragmentNumber_tHelper.read(packet));
        ShortWrapperSubmessageElement fis = new ShortWrapperSubmessageElement(packet.read_short());
        ShortWrapperSubmessageElement fsize = new ShortWrapperSubmessageElement(packet.read_short());
        LongWrapperSubmessageElement sampleSize = new LongWrapperSubmessageElement(packet.read_long());
        SerializedData serializedData = new SerializedData(packet, nextSubmessageHeader - packet.getCursorPosition());
        logInfo("decodeDataFrag()", "" + "Received DataFrag submessage SN=" + sn.getLongValue());
        return new DataFrag(readerId, writerId, sn, khp, khs, qos, fsn, fis, fsize, sampleSize, serializedData);
    }
