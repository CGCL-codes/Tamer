    private void clearChannels() {
        for (int i = 0; i < getComponentCount(); i++) {
            ChannelPanel cPanel = (ChannelPanel) getComponent(i);
            cPanel.clear();
            cPanel.getChannel().removePropertyChangeListener(this);
        }
        models.clear();
        this.removeAll();
    }
