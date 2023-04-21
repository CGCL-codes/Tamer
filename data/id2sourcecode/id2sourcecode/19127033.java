    protected void updatePendingPvHistory(ChannelModel source) {
        synchronized (lock) {
            pendingPv = true;
            pendingPvMap.put(source.getID(), source.getChannelName());
        }
    }
