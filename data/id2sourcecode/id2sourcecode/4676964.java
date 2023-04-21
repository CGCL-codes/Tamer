    public int[] getChannelDimLengths(String id) throws FormatException, IOException {
        setId(id);
        if (core.cLengths[series] == null) {
            core.cLengths[series] = new int[] { core.sizeC[series] };
        }
        return core.cLengths[series];
    }
