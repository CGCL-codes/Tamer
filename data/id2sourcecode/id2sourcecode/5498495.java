    public boolean isXChannel(DataChannel chan) {
        try {
            for (int i = 0; i < getGroupsSize(); i++) {
                DataGroup group = getGroup(i);
                DataChannel xchannel = getChannel(i, group.getXChannel());
                if (xchannel == chan) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
