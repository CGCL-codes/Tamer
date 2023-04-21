    @Override
    public EncodedSection encode(SCFChromatogram c, SCFHeader header) throws IOException {
        PositionStrategy positionStrategy = getPositionStrategyFor(c);
        final ChannelGroup channelGroup = c.getChannelGroup();
        ShortBuffer aPositions = channelGroup.getAChannel().getPositions();
        ShortBuffer cPositions = channelGroup.getCChannel().getPositions();
        ShortBuffer gPositions = channelGroup.getGChannel().getPositions();
        ShortBuffer tPositions = channelGroup.getTChannel().getPositions();
        byte sampleSize = positionStrategy.getSampleSize();
        final int numberOfSamples = aPositions.limit();
        final int bufferLength = numberOfSamples * 4 * sampleSize;
        ByteBuffer buffer = ByteBuffer.allocate(bufferLength);
        writePositionsToBuffer(positionStrategy, aPositions, cPositions, gPositions, tPositions, buffer);
        buffer.flip();
        header.setNumberOfSamples(numberOfSamples);
        header.setSampleSize(sampleSize);
        return new EncodedSection(buffer, Section.SAMPLES);
    }
