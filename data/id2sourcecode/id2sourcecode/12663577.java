    public Object peek(String instanceID, String channelName) {
        for (Iterator iterator = entries.iterator(); iterator.hasNext(); ) {
            Entry entry = (Entry) iterator.next();
            if (entry.getInstanceID() == null ? instanceID == null : entry.getInstanceID().equals(instanceID) && entry.getChannelName().equals(channelName)) {
                return entry.getMessage();
            }
        }
        return null;
    }
