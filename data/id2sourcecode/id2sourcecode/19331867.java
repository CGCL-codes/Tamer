    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        DefaultDataTableRow row = getRow(rowIndex);
        if (colIndex == Columns.DATA.getColumnIndex()) {
            return row.getDataAsString();
        } else if (colIndex == Columns.CHANNEL.getColumnIndex()) {
            return row.getChannel();
        } else if (colIndex == Columns.TIMESTAMP.getColumnIndex()) {
            return FormatUtils.dateTimeFormat.format(row.getTimestamp());
        } else {
            return "";
        }
    }
