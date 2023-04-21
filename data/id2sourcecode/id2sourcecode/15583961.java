    public void setValueAt(final Object value, final int row, final int col) {
        if (col == 1) {
            Channel channel = getChannel(row);
            channel.setName((String) value);
        }
    }
