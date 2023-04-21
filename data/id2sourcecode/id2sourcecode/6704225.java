    private void updateChannels() {
        channelsInSelectedGroup.clear();
        channelsNotInSelectedGroup.clear();
        if (selectedGroup != null) {
            for (Channel channel : selectedGroup.getChannels()) {
                channelsInSelectedGroup.add(channel);
            }
            if (notInAnyGroup.getValue()) {
                Groups groups = getGroups();
                Channels channels = context.getShow().getChannels();
                for (Channel channel : channels) {
                    if (!groups.includes(channel)) {
                        channelsNotInSelectedGroup.add(channel);
                    }
                }
            } else {
                Channels channels = context.getShow().getChannels();
                for (Channel channel : channels) {
                    if (!selectedGroup.includes(channel)) {
                        channelsNotInSelectedGroup.add(channel);
                    }
                }
            }
        } else {
            if (notInAnyGroup.getValue()) {
                Groups groups = getGroups();
                Channels channels = context.getShow().getChannels();
                for (Channel channel : channels) {
                    if (!groups.includes(channel)) {
                        channelsNotInSelectedGroup.add(channel);
                    }
                }
            } else {
                Channels channels = context.getShow().getChannels();
                for (Channel channel : channels) {
                    channelsNotInSelectedGroup.add(channel);
                }
            }
        }
        inGroupTableModel.fireTableDataChanged();
        notInGroupTableModel.fireTableDataChanged();
        updateActions();
    }
