    public Object getValueAt(final int row, final int col) {
        Channel channel = getChannel(row);
        Object value;
        switch(col) {
            case 0:
                value = channel.getId() + 1;
                break;
            case 1:
                value = channel.getName();
                break;
            default:
                value = "?";
        }
        return value;
    }
