    private void actionAddToGroup() {
        int[] channelIndexes = getChannelsNotInGroup();
        int[] rows = notInGroupTable.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            int channelIndex = channelIndexes[rows[i]];
            Channel channel = getContext().getShow().getChannels().get(channelIndex);
            getSelectedGroup().add(channel);
        }
        updateWidgets();
        inGroupTableModel.fireTableDataChanged();
        notInGroupTableModel.fireTableDataChanged();
    }
