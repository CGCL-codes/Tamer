    private Gap decodeGap() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(_packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(_packet));
        SequenceNumber gapStart = new SequenceNumber(SequenceNumber_tHelper.read(_packet));
        SequenceNumberSet gapList = SequenceNumberSet.read(_packet);
        GlobalProperties.logger.log(Logger.INFO, CDRMessageProcessorDEBUG.class, "decodeGap()", "" + "Received Gap submessage gapStart=" + gapStart.getLongValue());
        return new Gap(readerId, writerId, gapStart, gapList);
    }
