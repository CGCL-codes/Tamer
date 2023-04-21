    protected JComponent getChannelComponent() {
        if (channels.size() == 0) {
            return null;
        }
        JPanel channelBar = new JPanel();
        channelBar.setOpaque(false);
        channelBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        Iterator<String> i = channels.iterator();
        while (i.hasNext()) {
            String channelName = i.next();
            if (showChannelsInTitle) {
                channelBar.add(new ChannelTitle(channelName));
            }
        }
        return channelBar;
    }
