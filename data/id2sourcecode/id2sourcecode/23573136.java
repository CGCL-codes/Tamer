    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        StateDataTableRow row = getRow(rowIndex);
        if (colIndex == Columns.STATE.getColumnIndex()) {
            return row.toString();
        } else if (colIndex == Columns.CHANNEL.getColumnIndex()) {
            return row.getChannel();
        } else if (colIndex == Columns.TIMESTAMP.getColumnIndex()) {
            return FormatUtils.dateTimeFormat.format(row.getTimestamp());
        } else {
            return "";
        }
    }
