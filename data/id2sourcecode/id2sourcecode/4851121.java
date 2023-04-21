    public Object getValueAt(final int row, final int col) {
        Object value = "?";
        int columnIndex = getColumnIndex(col);
        if (isRowTiming(row)) {
            value = "";
        } else {
            if (isRowSubmaster(row)) {
                int submasterIndex = getSubmasterIndex(row);
                value = getValueSubmasterAt(submasterIndex, columnIndex);
            } else {
                int channelIndex = getChannelIndex(row);
                value = getValueChannelAt(channelIndex, columnIndex);
            }
        }
        return value;
    }
