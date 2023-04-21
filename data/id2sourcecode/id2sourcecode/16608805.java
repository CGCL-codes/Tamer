    protected List<ChannelRef> fetchChannelRefs(final String monitorName) {
        final Vector channelInfo = SERVICE.getChannelInfo(monitorName);
        final List<ChannelRef> channelRefs = new ArrayList<ChannelRef>(channelInfo.size());
        for (final Object record : channelInfo) {
            final Map channelRecord = (Map) record;
            channelRefs.add(ChannelRef.getInstanceFromInfoRecord(channelRecord));
        }
        return channelRefs;
    }
