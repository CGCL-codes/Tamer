    public void updateNodeId(String newValue, String previousValue) {
        String upChannels = getUpChannels(previousValue);
        if (upChannels != null) {
            String[] fields = upChannels.split(",");
            for (String f : fields) {
                getChannel(f).setUpNodeId(newValue);
            }
            upNodeMap.remove(previousValue);
            upNodeMap.put(newValue, upChannels);
        }
        String downChannels = getDownChannels(previousValue);
        if (downChannels != null) {
            String[] fields = downChannels.split(",");
            for (String f : fields) {
                getChannel(f).setDownNodeId(newValue);
            }
            downNodeMap.remove(previousValue);
            downNodeMap.put(newValue, downChannels);
        }
    }
