    public Object getValueAt(final int row, final int column) {
        final Channel channel = getChannel(row);
        switch(column) {
            case LABEL_COLUMN:
                return getConnectionText(channel, getRowLabel(row));
            case SIGNAL_COLUMN:
                return (channel != null) ? channel.channelName() : "";
            default:
                return "";
        }
    }
