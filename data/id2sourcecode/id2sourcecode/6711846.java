    private int getMaxPositionsValue(SCFChromatogram c) {
        ChannelGroup group = c.getChannelGroup();
        ShortBuffer aPositions = group.getAChannel().getPositions();
        ShortBuffer cPositions = group.getCChannel().getPositions();
        ShortBuffer gPositions = group.getGChannel().getPositions();
        ShortBuffer tPositions = group.getTChannel().getPositions();
        aPositions.rewind();
        cPositions.rewind();
        gPositions.rewind();
        tPositions.rewind();
        int max = Collections.max(Arrays.asList(getMaxValueFor(aPositions), getMaxValueFor(cPositions), getMaxValueFor(gPositions), getMaxValueFor(tPositions)));
        aPositions.rewind();
        cPositions.rewind();
        gPositions.rewind();
        tPositions.rewind();
        return max;
    }
