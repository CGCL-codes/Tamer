    public void setFocus(final CellFocusListener cellFocusListener, final int row, final int column) {
        if (!isRowTiming(row)) {
            int cueIndex = getCueIndex(column);
            if (isRowSubmaster(row)) {
                int submasterIndex = getSubmasterIndex(row);
                cellFocusListener.setFocusSubmaster(cueIndex, submasterIndex);
            } else {
                int channelIndex = row - getShow().getNumberOfSubmasters() - 1;
                int firstRow = getShow().getNumberOfSubmasters() + 1;
                int lastRow = getRowCount();
                int channelIndexes[] = new int[lastRow - firstRow];
                for (int i = firstRow; i < lastRow; i++) {
                    channelIndexes[i - firstRow] = getChannelIndex(i);
                }
                cellFocusListener.setFocusChannel(cueIndex, channelIndex, channelIndexes);
            }
        }
    }
