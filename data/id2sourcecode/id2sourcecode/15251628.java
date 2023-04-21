    public int[] getChannelDimLengths() {
        FormatTools.assertId(currentId, true, 1);
        return core.cLengths[getSeries()];
    }
