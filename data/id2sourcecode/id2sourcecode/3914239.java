    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source.getClass().equals(ChannelCheckBoxMenuItem.class)) {
            ChannelCheckBoxMenuItem item = (ChannelCheckBoxMenuItem) source;
            if (item.isSelected()) {
                this.filter.addChannel(item.channelId);
            } else {
                this.filter.removeChannel(item.channelId);
            }
        } else {
            this.filter.removeAllChannels(false);
            Object[] arItems = this.channelMap.values().toArray();
            for (int nA = 0; nA < arItems.length; nA++) {
                ((ChannelCheckBoxMenuItem) arItems[nA]).setSelected(false);
            }
            if (((ChannelsetMenuItem) source).set != null) {
                Object[] arChannels = ((ChannelsetMenuItem) source).set.getChannels().toArray();
                for (int nA = 0; nA < arChannels.length; nA++) {
                    TVChannelsSet.Channel channel = (TVChannelsSet.Channel) arChannels[nA];
                    ChannelCheckBoxMenuItem item = (ChannelCheckBoxMenuItem) this.channelMap.get(channel.getChannelID());
                    if (item != null) {
                        item.setSelected(true);
                        this.filter.addChannel(item.channelId, false);
                    }
                }
            }
            this.filter.notifyFilterChange();
        }
    }
