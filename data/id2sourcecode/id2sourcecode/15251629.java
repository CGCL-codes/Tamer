    public String[] getChannelDimTypes() {
        FormatTools.assertId(currentId, true, 1);
        return core.cTypes[getSeries()];
    }
