    @Override
    public int getTileCompWidth(int t, int c) {
        return src.getTileCompWidth(t, csMap.getChannelDefinition(c));
    }
