    public boolean isAtCapacity(String channelName) {
        Channel c = getChannel(channelName);
        if (c != null) {
            if (c.getLimit() != 0 && c.numMembers() >= c.getLimit()) {
                return true;
            }
        }
        return false;
    }
