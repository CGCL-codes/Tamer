    public boolean showListWindow() {
        if (!MyChannelListFrame.getChannelListFrame().isVisible()) {
            desktopPane.add(MyChannelListFrame.getChannelListFrame());
            MyChannelListFrame.getChannelListFrame().updateList();
            MyChannelListFrame.getChannelListFrame().setVisible(true);
            try {
                MyChannelListFrame.getChannelListFrame().setSelected(true);
            } catch (PropertyVetoException e) {
            }
            MyChannelListFrame.getChannelListFrame().moveToFront();
            return true;
        }
        return false;
    }
