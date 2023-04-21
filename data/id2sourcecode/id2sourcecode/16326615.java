    public boolean isSelected(final int row, final int column) {
        boolean selected = false;
        if (!isDummyColumn(column)) {
            LightCueDetail detail = getDetail(column);
            if (isRowTiming(row)) {
                selected = detail.isTimingSelected();
            } else {
                if (isRowChannel(row)) {
                    selected = detail.getChannelLevel(getChannelIndex(row)).isSelected();
                } else if (isRowSubmaster(row)) {
                    selected = detail.getSubmasterLevel(getSubmasterIndex(row)).isSelected();
                }
            }
        }
        return selected;
    }
