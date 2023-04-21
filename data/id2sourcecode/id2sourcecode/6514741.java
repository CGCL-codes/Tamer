    @Override
    public DataBlk getCompData(DataBlk out, int c) {
        return src.getCompData(out, csMap.getChannelDefinition(c));
    }
