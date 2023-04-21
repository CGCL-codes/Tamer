    public void setXChannel(DataChannel chan) {
        try {
            for (int i = 0; i < getGroupsSize(); i++) {
                DataGroup group = getGroup(i);
                for (int j = 0; j < getChannelsSize(i); j++) {
                    DataChannel xchannel = getChannel(i, j);
                    if (xchannel == chan) {
                        group.setXChannel(j);
                    }
                }
            }
        } catch (Exception e) {
        }
    }
