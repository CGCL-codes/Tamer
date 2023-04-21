    public String[] getChannelDimTypes() {
        FormatTools.assertId(currentId, true, 2);
        return getReader().getChannelDimTypes();
    }
