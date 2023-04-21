    @Override
    public void setValueAt(final Object value, final int row, final int col) {
        int columnIndex = getColumnIndex(col);
        if (isRowSubmaster(row)) {
            setValueSubmasterAt(value, getSubmasterIndex(row), columnIndex);
        } else {
            setValueChannelAt(value, getChannelIndex(row), columnIndex);
        }
    }
