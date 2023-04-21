    protected List<InputChannelGroupInterface> getInputChannelGroups(final Map<String, InputChannelInterface> internalNameToInputChannel) throws IOException {
        ResultSet resultSet = readData(getInputChannelGroupsStatement());
        List<InputChannelGroupInterface> output = new ArrayList<InputChannelGroupInterface>();
        HashMap<String, InputChannelGroup> groupMap = new HashMap<String, InputChannelGroup>();
        if (resultSet == null) {
            System.err.println("Null or empty group list?");
            return output;
        }
        try {
            while (resultSet.next()) {
                final String groupName = resultSet.getString("groupname");
                final String groupLabel = resultSet.getString("grouplabel");
                final String targetChannelName = resultSet.getString("channelname");
                InputChannelGroup channelGroup = groupMap.get(groupName);
                if (channelGroup == null) {
                    channelGroup = new InputChannelGroup(groupName, groupLabel, "");
                    groupMap.put(groupName, channelGroup);
                }
                final InputChannelInterface channel = internalNameToInputChannel.get(targetChannelName);
                channelGroup.addChannel(channel);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        output.addAll(groupMap.values());
        return output;
    }
