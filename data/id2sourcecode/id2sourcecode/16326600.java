    public Object getValueAt(final int row, final int col) {
        Object value = "";
        if (!isDummyColumn(col)) {
            if (isRowTiming(row)) {
                value = getDetail(col).getTiming();
            } else {
                LightCueDetail detail = getDetail(col);
                if (isRowSubmaster(row)) {
                    value = detail.getSubmasterLevel(getSubmasterIndex(row));
                } else {
                    value = detail.getChannelLevel(getChannelIndex(row));
                }
            }
        }
        return value;
    }
