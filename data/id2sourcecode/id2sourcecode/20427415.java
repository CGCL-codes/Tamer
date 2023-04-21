    public void intervalAdded(ListDataEvent e) {
        int index0 = e.getIndex0();
        int index1 = e.getIndex1();
        for (int i = index0; i <= index1; i++) {
            Channel channel = channels.getChannel(i);
            ChannelPanel cPanel = createChannelPanel(channel);
            this.add(cPanel, i);
        }
    }
