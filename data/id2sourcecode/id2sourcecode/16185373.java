    private AckNack decodeAckNack() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(packet));
        SequenceNumberSet sns = SequenceNumberSet.read(packet);
        Count count = new Count(Count_tHelper.read(packet));
        logInfo("decodeAckNack()", "" + "Received AckNack submessage");
        return new AckNack(readerId, writerId, sns, count);
    }
