    private AckNack decodeAckNack() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(_packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(_packet));
        SequenceNumberSet sns = SequenceNumberSet.read(_packet);
        Count count = new Count(Count_tHelper.read(_packet));
        GlobalProperties.logger.log(Logger.INFO, CDRMessageProcessorDEBUG.class, "decodeAckNack()", "" + "Received AckNack submessage");
        return new AckNack(readerId, writerId, sns, count);
    }
