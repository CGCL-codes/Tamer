    private Gap decodeGap() throws MalformedSubmessageException {
        EntityId readerId = new EntityId(EntityId_tHelper.read(packet));
        EntityId writerId = new EntityId(EntityId_tHelper.read(packet));
        SequenceNumber gapStart = new SequenceNumber(SequenceNumber_tHelper.read(packet));
        SequenceNumberSet gapList = SequenceNumberSet.read(packet);
        logInfo("decodeGap()", "" + "Received Gap submessage gapStart=" + gapStart.getLongValue());
        return new Gap(readerId, writerId, gapStart, gapList);
    }
