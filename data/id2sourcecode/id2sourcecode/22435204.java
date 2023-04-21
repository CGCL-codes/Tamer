    private void rebuildChannelsUI(final ChannelList channels) {
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.getChannel(i);
            ChannelPanel cPanel = createChannelPanel(channel);
            this.add(cPanel);
        }
    }
