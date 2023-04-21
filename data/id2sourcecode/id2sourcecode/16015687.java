    public Hashtable getChannelDefinitions() {
        Hashtable channelDefinitions;
        if (parent != null) {
            channelDefinitions = parent.getChannelDefinitions();
        } else {
            channelDefinitions = new Hashtable();
        }
        return channelDefinitions;
    }
