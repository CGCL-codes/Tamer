    @Override
    public int getFixedPoint(int c) {
        return src.getFixedPoint(csMap.getChannelDefinition(c));
    }
