    public List<Channel> getSelectedChannels() {
        List<Channel> channels = new ArrayList<Channel>();
        int[] rows = channelSelectionModel.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            Channel channel = channelTableModel.getChannel(rows[i]);
            channels.add(channel);
        }
        return channels;
    }
