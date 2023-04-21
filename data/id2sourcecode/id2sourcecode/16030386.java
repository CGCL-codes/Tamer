    public void selectedChannelGroupChanged(final BrowserController controller, final ChannelGroup newGroup) {
        if (newGroup != null) {
            ChannelWrapper[] wrappers = newGroup.getChannelWrappers();
            setSignals(BrowserController.convertToPVs(wrappers));
        } else {
            setSignals(new String[0]);
        }
    }
