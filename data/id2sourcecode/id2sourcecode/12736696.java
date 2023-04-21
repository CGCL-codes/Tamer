    @Override
    protected void writeBasesDataToBuffer(ByteBuffer buffer, SCFChromatogram c, int numberOfBases) {
        final ChannelGroup channelGroup = c.getChannelGroup();
        final ByteBuffer aConfidence = ByteBuffer.wrap(channelGroup.getAChannel().getConfidence().getData());
        final ByteBuffer cConfidence = ByteBuffer.wrap(channelGroup.getCChannel().getConfidence().getData());
        final ByteBuffer gConfidence = ByteBuffer.wrap(channelGroup.getGChannel().getConfidence().getData());
        final ByteBuffer tConfidence = ByteBuffer.wrap(channelGroup.getTChannel().getConfidence().getData());
        bulkPutPeaks(buffer, c.getPeaks().getData());
        bulkPut(buffer, aConfidence, numberOfBases);
        bulkPut(buffer, cConfidence, numberOfBases);
        bulkPut(buffer, gConfidence, numberOfBases);
        bulkPut(buffer, tConfidence, numberOfBases);
        bulkPut(buffer, c.getNucleotideSequence());
        bulkPutWithPadding(buffer, c.getSubstitutionConfidence(), numberOfBases);
        bulkPutWithPadding(buffer, c.getInsertionConfidence(), numberOfBases);
        bulkPutWithPadding(buffer, c.getDeletionConfidence(), numberOfBases);
    }
