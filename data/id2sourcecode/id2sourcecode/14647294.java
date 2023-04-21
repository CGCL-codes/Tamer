    public static String getChannelsConnectedTo(Channels channels, Node node) {
        String upChannels = channels.getUpChannels(node.getId());
        String downChannels = channels.getDownChannels(node.getId());
        if ((upChannels == null) && (downChannels == null)) {
            return null;
        }
        if (upChannels == null) {
            return downChannels;
        }
        if (downChannels == null) {
            return upChannels;
        }
        return downChannels + "," + upChannels;
    }
