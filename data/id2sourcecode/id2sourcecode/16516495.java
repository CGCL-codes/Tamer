    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        ValueDataTableRow row = getRow(rowIndex);
        if (colIndex == Columns.VALUE.getColumnIndex()) {
            return row.getValue();
        } else if (colIndex == Columns.UNIT.getColumnIndex()) {
            return row.getUnit().toString();
        } else if (colIndex == Columns.CHANNEL.getColumnIndex()) {
            return row.getChannel();
        } else if (colIndex == Columns.TIMESTAMP.getColumnIndex()) {
            return FormatUtils.dateTimeFormat.format(row.getTimestamp());
        } else {
            return "";
        }
    }
