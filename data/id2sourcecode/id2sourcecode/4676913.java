    public int[] getChannelDimLengths() {
        FormatTools.assertId(currentId, true, 1);
        if (core.cLengths[series] == null) {
            core.cLengths[series] = new int[] { core.sizeC[series] };
        }
        return core.cLengths[series];
    }
