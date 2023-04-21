    public JPanel getChannelPanel(String name) {
        if (channelPanel == null) {
            channelPanel = new JPanel();
            channelPanel.add(new JLabel(name));
            channelLabel = new JLabel();
            channelLabel.setFont(new Font(UITools.getString("dw.fontlabel"), Font.PLAIN, 14));
            setChannelLabel();
            channelPanel.add(channelLabel);
        }
        return channelPanel;
    }
