    private void deactivateLevelValueAt(final int row, final int col) {
        if (isRowSubmaster(row)) {
            int submasterIndex = getSubmasterIndex(row);
            getCues().deactivateCueSubmaster(getCueIndex(col), submasterIndex);
        } else {
            int channelIndex = getChannelIndex(row);
            getCues().deactivateChannel(getCueIndex(col), channelIndex);
        }
    }
