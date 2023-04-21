    public List<String> getChannelsForNodeId(String nodeId) {
        String downChannels = channels.getDownChannels(nodeId);
        String upChannels = channels.getUpChannels(nodeId);
        ArrayList<String> channelIds = new ArrayList<String>();
        parseIdsToList(downChannels, channelIds);
        parseIdsToList(upChannels, channelIds);
        return channelIds;
    }
