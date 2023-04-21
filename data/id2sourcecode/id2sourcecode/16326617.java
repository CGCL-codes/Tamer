    public void setSelected(final int row, final int column, final boolean selected) {
        if (!isDummyColumn(column)) {
            LightCueDetail detail = getDetail(column);
            if (isRowTiming(row)) {
                detail.setTimingSelected(selected);
                fireTableCellUpdated(row, column);
            } else {
                if (isRowChannel(row)) {
                    CueChannelLevel level = detail.getChannelLevel(getChannelIndex(row));
                    boolean oldSelected = level.isSelected();
                    if (selected != oldSelected) {
                        level.setSelected(selected);
                        fireTableCellUpdated(row, column);
                    }
                } else if (isRowSubmaster(row)) {
                    CueSubmasterLevel level = detail.getSubmasterLevel(getSubmasterIndex(row));
                    boolean oldSelected = level.isSelected();
                    if (selected != oldSelected) {
                        level.setSelected(selected);
                        fireTableCellUpdated(row, column);
                    }
                }
            }
        }
    }
