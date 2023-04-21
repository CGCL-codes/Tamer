    public List<ChannelRef> getChannelRefs(final String monitorID) {
        synchronized (CHANNEL_REF_MAP) {
            return CHANNEL_REF_MAP.get(monitorID);
        }
    }
