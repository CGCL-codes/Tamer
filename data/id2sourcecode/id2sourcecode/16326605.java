    private void resetLevelValueAt(final int row, final int col) {
        if (isRowSubmaster(row)) {
            int submasterIndex = getSubmasterIndex(row);
            getCues().resetSubmaster(getCueIndex(col), submasterIndex);
        } else {
            int channelIndex = getChannelIndex(row);
            getCues().resetChannel(getCueIndex(col), channelIndex);
        }
    }
