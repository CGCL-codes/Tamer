    public void selectedChannelGroupChanged(final BrowserModel model, final ChannelGroup newGroup) {
        if (newGroup != null) {
            ChannelWrapper[] wrappers = newGroup.getChannelWrappers();
            setSelectedSignals(convertToPVs(wrappers));
        } else {
            setSelectedSignals(new String[0]);
        }
        _proxy.selectedChannelGroupChanged(this, newGroup);
    }
