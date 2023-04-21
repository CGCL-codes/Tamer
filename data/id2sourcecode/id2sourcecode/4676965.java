    public String[] getChannelDimTypes(String id) throws FormatException, IOException {
        setId(id);
        if (core.cTypes[series] == null) {
            core.cTypes[series] = new String[] { FormatTools.CHANNEL };
        }
        return core.cTypes[series];
    }
