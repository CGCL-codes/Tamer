    public Object getElementAt(int index) {
        if (index == 0) {
            return Channel.MASTER;
        }
        if (channels == null) {
            return null;
        }
        return channels.getChannel(index - 1).getName();
    }
