    private void setLevelValueAt(final float levelValue, final int row, final int col) {
        if (isRowSubmaster(row)) {
            int submasterIndex = getSubmasterIndex(row);
            getCues().setCueSubmaster(getCueIndex(col), submasterIndex, levelValue);
        } else {
            int channelIndex = getChannelIndex(row);
            getCues().setChannel(getCueIndex(col), channelIndex, levelValue);
        }
    }
