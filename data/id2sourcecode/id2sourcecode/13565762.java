    private HeartBeat decodeHeartBeat() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(_packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(_packet));
        SequenceNumber firstSN = new SequenceNumber(SequenceNumber_tHelper.read(_packet));
        SequenceNumber lastSN = new SequenceNumber(SequenceNumber_tHelper.read(_packet));
        Count count = new Count(Count_tHelper.read(_packet));
        GlobalProperties.logger.log(Logger.INFO, CDRMessageProcessorDEBUG.class, "decodeHeartBeat()", "" + "Received HeartBeat submessage SN=" + firstSN.getLongValue());
        return new HeartBeat(readerId, writerId, firstSN, lastSN, count);
    }
