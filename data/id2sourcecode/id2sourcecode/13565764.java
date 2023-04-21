    private HeartBeatFrag decodeHeartBeatFrag() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(_packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(_packet));
        SequenceNumber writerSN = new SequenceNumber(SequenceNumber_tHelper.read(_packet));
        FragmentNumber fn = new FragmentNumber(FragmentNumber_tHelper.read(_packet));
        Count count = new Count(Count_tHelper.read(_packet));
        GlobalProperties.logger.log(Logger.INFO, CDRMessageProcessorDEBUG.class, "decodeHeartBeatFrag()", "" + "Received HeartBeatFrag submessage writerSN=" + writerSN.getLongValue());
        return new HeartBeatFrag(readerId, writerId, writerSN, fn, count);
    }
