    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.addTab(Messages.getString("ChannelEditor.17"), new ImageIcon(getClass().getResource("/org/javalobby/icons/16x16/GreenFlag.gif")), getChannelParkingPanel(), null);
            jTabbedPane.addTab(Messages.getString("ChannelEditor.19"), new ImageIcon(getClass().getResource("/org/javalobby/icons/16x16/RedFlag.gif")), getChannelDeletedPanel(), null);
        }
        return jTabbedPane;
    }
