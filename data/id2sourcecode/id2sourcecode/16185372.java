    private HeartBeat decodeHeartBeat() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(packet));
        SequenceNumber firstSN = new SequenceNumber(SequenceNumber_tHelper.read(packet));
        SequenceNumber lastSN = new SequenceNumber(SequenceNumber_tHelper.read(packet));
        Count count = new Count(Count_tHelper.read(packet));
        logInfo("decodeHeartBeat()", "" + "Received HeartBeat submessage SN=" + firstSN.getLongValue());
        return new HeartBeat(readerId, writerId, firstSN, lastSN, count);
    }
