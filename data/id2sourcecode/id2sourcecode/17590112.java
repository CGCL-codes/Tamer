    public List<Channel> getSelectedChannels() {
        List<Channel> channels = newArrayList();
        int[] rows = channelSelectionModel.getSelectedRows();
        for (int row : rows) {
            Channel channel = channelTableModel.getChannel(row);
            channels.add(channel);
        }
        return channels;
    }
