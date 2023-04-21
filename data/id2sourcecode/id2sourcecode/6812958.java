    public Object getValueAt(final int row, final int col) {
        Object value;
        Dimmer dimmer = context.getShow().getDimmers().get(row);
        Channels channels = context.getShow().getChannels();
        switch(col) {
            case 0:
                value = "" + (row + 1);
                break;
            case 1:
                value = dimmer.getName();
                break;
            case 2:
                if (dimmer.getChannelId() == -1) {
                    Channel c = new Channel(0, "not patched");
                    c.setId(-1);
                    value = c;
                } else {
                    value = channels.get(dimmer.getChannelId());
                }
                break;
            default:
                value = "?";
        }
        return value;
    }
