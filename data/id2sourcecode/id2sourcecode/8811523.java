    public List<ChannelMonitor> getChannelMonitors() {
        final List<ChannelMonitor> channelMonitors = new ArrayList<ChannelMonitor>();
        for (final NodeMonitor nodeMonitor : NODE_MONITORS) {
            channelMonitors.addAll(nodeMonitor.getChannelMonitors());
        }
        return channelMonitors;
    }
