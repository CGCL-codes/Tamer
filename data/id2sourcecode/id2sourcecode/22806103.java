    public OJIRCChannel getTheChannel(String channame) {
        for (int i = 0; i < getChannelsCount(); i++) {
            if (((OJIRCChannel) channels.elementAt(i)).getChannelName().equals(channame)) return (OJIRCChannel) channels.elementAt(i);
        }
        return null;
    }
