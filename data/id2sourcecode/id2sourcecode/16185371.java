    private NoKeyDataFrag decodeNoKeyDataFrag() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(packet));
        SequenceNumber sn = new SequenceNumber(SequenceNumber_tHelper.read(packet));
        ParameterList inlineQoS = null;
        if (BitUtility.getFlagAt(flags, 1)) {
            inlineQoS = new ParameterList(packet);
        }
        FragmentNumber fragmentStartingNum = new FragmentNumber(FragmentNumber_tHelper.read(packet));
        short fragmentsInSubmessage = packet.read_short();
        short fragmentSize = packet.read_short();
        int sampleSize = packet.read_long();
        SerializedData serializedData = new SerializedData(packet, nextSubmessageHeader - packet.getCursorPosition());
        logInfo("decodeNoKeyDataFrag()", "" + "Received NoKeyDataFrag submessage SN=" + sn.getLongValue());
        return new NoKeyDataFrag(readerId, writerId, sn, inlineQoS, fragmentStartingNum, fragmentsInSubmessage, fragmentSize, sampleSize, serializedData);
    }
