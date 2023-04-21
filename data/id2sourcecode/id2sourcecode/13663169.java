    public int[] getChannelDimLengths() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getChannelDimLengths();
    }
