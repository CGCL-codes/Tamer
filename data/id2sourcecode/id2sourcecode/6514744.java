    @Override
    public int getNomRangeBits(int c) {
        return src.getNomRangeBits(csMap.getChannelDefinition(c));
    }
