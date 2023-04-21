    private ChannelServer getChannelServer() {
        ServiceTracker tracker = new ServiceTracker(context, ChannelServer.class.getName(), null);
        tracker.open(true);
        Object tracked = null;
        try {
            tracked = getTrackedService(tracker);
            if (tracked == null) throw new RuntimeException("Couldn't find ChannelServer in the OSGi framework");
        } catch (Exception e) {
            throw new RuntimeException("No ChannelServer is registered with the OSGi framework", e);
        }
        return (ChannelServer) tracked;
    }
