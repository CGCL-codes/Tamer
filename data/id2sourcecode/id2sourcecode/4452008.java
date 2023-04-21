    public ChannelPropertyPanel getChannelPropertyPanel() {
        if (channelPropertyPanel == null) {
            channelPropertyPanel = new ChannelPropertyPanel();
            channelPropertyPanel.setPreferredSize(new java.awt.Dimension(440, 200));
        }
        return channelPropertyPanel;
    }
