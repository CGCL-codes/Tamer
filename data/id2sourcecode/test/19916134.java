    protected void writeBasesDataToBuffer(ByteBuffer buffer, SCFChromatogram c, int numberOfBases) {
        Sequence<ShortSymbol> peaks = c.getPeaks().getData();
        final ChannelGroup channelGroup = c.getChannelGroup();
        final ByteBuffer aConfidence = ByteBuffer.wrap(channelGroup.getAChannel().getConfidence().getData());
        final ByteBuffer cConfidence = ByteBuffer.wrap(channelGroup.getCChannel().getConfidence().getData());
        final ByteBuffer gConfidence = ByteBuffer.wrap(channelGroup.getGChannel().getConfidence().getData());
        final ByteBuffer tConfidence = ByteBuffer.wrap(channelGroup.getTChannel().getConfidence().getData());
        final Sequence<Nucleotide> basecalls = c.getNucleotideSequence();
        final ByteBuffer substitutionConfidence = getOptionalField(c.getSubstitutionConfidence());
        final ByteBuffer insertionConfidence = getOptionalField(c.getInsertionConfidence());
        final ByteBuffer deletionConfidence = getOptionalField(c.getDeletionConfidence());
        for (int i = 0; i < numberOfBases; i++) {
            buffer.putInt(peaks.get(i).getValue().intValue());
            buffer.put(aConfidence.get());
            buffer.put(cConfidence.get());
            buffer.put(gConfidence.get());
            buffer.put(tConfidence.get());
            buffer.put((byte) basecalls.get(i).getCharacter().charValue());
            handleOptionalField(buffer, substitutionConfidence);
            handleOptionalField(buffer, insertionConfidence);
            handleOptionalField(buffer, deletionConfidence);
        }
    }
