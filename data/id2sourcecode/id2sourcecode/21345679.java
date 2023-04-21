    public boolean isOnChannel(String channel) {
        for (Enumeration iter = getChannelNames(); iter.hasMoreElements(); ) {
            if (channel.equalsIgnoreCase((String) iter.nextElement())) {
                return true;
            }
        }
        return false;
    }
