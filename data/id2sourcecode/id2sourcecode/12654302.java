    public void updateItems() {
        List channels = this.dialog.getHandle().getChannels();
        this.removeChannelsAfter(channels.size());
        for (int i = 0; i < channels.size(); i++) {
            TGChannel channel = (TGChannel) channels.get(i);
            TGChannelItem tgChannelItem = getOrCreateChannelItemAt(i);
            tgChannelItem.setChannel(channel);
            tgChannelItem.updateItems();
        }
    }
