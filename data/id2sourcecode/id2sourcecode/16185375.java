    private NackFrag decodeNackFrag() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(packet));
        SequenceNumber writerSN = new SequenceNumber(SequenceNumber_tHelper.read(packet));
        FragmentNumberSet fragmentNumberState = FragmentNumberSet.read(packet);
        Count count = new Count(Count_tHelper.read(packet));
        logInfo("decodeNackFrag()", "" + "Received NackFrag submessage writerSN=" + writerSN.getLongValue());
        return new NackFrag(readerId, writerId, writerSN, fragmentNumberState, count);
    }
