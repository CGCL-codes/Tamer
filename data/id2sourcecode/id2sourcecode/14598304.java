    public void channelSelected(ChannelSelectionEvent e) {
        if (e.isRoot()) {
            channel = ROOT_CHANNEL;
        } else {
            channel = e.getChannelName();
        }
        children = e.getChildren();
        updatePanel();
    }
