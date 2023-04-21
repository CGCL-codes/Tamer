    public int add(ChannelListItem channellistitem) {
        int i = super.add(channellistitem);
        channelNameLow2channelItem.put(channellistitem.getChannelNameLowercased(), channellistitem);
        return i;
    }
