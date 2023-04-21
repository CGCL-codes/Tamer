    public String[] getChannelDimTypes() {
        FormatTools.assertId(currentId, true, 1);
        if (core.cTypes[series] == null) {
            core.cTypes[series] = new String[] { FormatTools.CHANNEL };
        }
        return core.cTypes[series];
    }
