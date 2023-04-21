    private void removeSubChannnel() {
        if (selectedChannelPanel == null) {
            return;
        }
        Channel channel = selectedChannelPanel.getChannel();
        channel.removePropertyChangeListener(this);
        subChannels.removeChannel(channel);
        models.remove(selectedChannelPanel.getChannelOutModel());
        selectedChannelPanel.clear();
        this.remove(selectedChannelPanel);
        selectedChannelPanel = null;
        String removedChannel = channel.getName();
        reconcileSubChannelRemoveInBlueArrangement(removedChannel);
    }
