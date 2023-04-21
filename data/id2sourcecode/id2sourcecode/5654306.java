    private void updateGroupChannels() {
        final ChannelGroup group = getSelectedChannelGroup();
        if (group != null) {
            final List<String> channelNames = new ArrayList<String>();
            for (final Channel channel : group.getChannels()) {
                channelNames.add(channel.channelName());
            }
            channelNames.addAll(PENDING_GROUP_SIGNALS);
            Collections.sort(channelNames);
            CHANNEL_TABLE_MODEL.setRecords(channelNames);
        } else {
            CHANNEL_TABLE_MODEL.setRecords(new ArrayList<String>());
        }
    }
