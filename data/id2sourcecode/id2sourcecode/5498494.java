    public DataChannel getXChannel(DataChannel chan) {
        try {
            for (int i = 0; i < getGroupsSize(); i++) {
                DataGroup group = getGroup(i);
                DataChannel xchannel = getChannel(i, group.getXChannel());
                for (int j = 0; j < getChannelsSize(i); j++) {
                    DataChannel channel = getChannel(i, j);
                    if (channel == chan) {
                        return xchannel;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
