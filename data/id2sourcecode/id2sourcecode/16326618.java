    public String getRowText(final int row) {
        String value = "";
        if (isRowChannel(row)) {
            int index = getChannelIndex(row);
            Channel channel = getContext().getShow().getChannels().get(index);
            value = "[" + (channel.getId() + 1) + "] " + channel.getName();
        } else if (isRowSubmaster(row)) {
            int index = getSubmasterIndex(row);
            Submaster submaster = getContext().getShow().getSubmasters().get(index);
            value = "[S" + (submaster.getId() + 1) + "] " + submaster.getName();
        }
        return value;
    }
