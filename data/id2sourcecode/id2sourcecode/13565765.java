    private NackFrag decodeNackFrag() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(_packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(_packet));
        SequenceNumber writerSN = new SequenceNumber(SequenceNumber_tHelper.read(_packet));
        FragmentNumberSet fragmentNumberState = FragmentNumberSet.read(_packet);
        Count count = new Count(Count_tHelper.read(_packet));
        GlobalProperties.logger.log(Logger.INFO, CDRMessageProcessorDEBUG.class, "decodeNackFrag()", "" + "Received NackFrag submessage writerSN=" + writerSN.getLongValue());
        return new NackFrag(readerId, writerId, writerSN, fragmentNumberState, count);
    }
