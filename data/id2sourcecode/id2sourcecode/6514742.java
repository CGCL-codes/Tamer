    @Override
    public DataBlk getInternCompData(DataBlk out, int c) {
        return src.getInternCompData(out, csMap.getChannelDefinition(c));
    }
