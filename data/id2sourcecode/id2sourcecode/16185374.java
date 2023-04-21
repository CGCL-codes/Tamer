    private HeartBeatFrag decodeHeartBeatFrag() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(packet));
        SequenceNumber writerSN = new SequenceNumber(SequenceNumber_tHelper.read(packet));
        FragmentNumber fn = new FragmentNumber(FragmentNumber_tHelper.read(packet));
        Count count = new Count(Count_tHelper.read(packet));
        logInfo("decodeHeartBeatFrag()", "" + "Received HeartBeatFrag submessage writerSN=" + writerSN.getLongValue());
        return new HeartBeatFrag(readerId, writerId, writerSN, fn, count);
    }
