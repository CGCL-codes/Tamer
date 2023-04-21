    @Override
    public int getTileCompHeight(int t, int c) {
        return src.getTileCompHeight(t, csMap.getChannelDefinition(c));
    }
