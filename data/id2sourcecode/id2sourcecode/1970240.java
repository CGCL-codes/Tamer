    public Vector getChannelInfo(final String monitorName) {
        final TripMonitor tripMonitor = getTripMonitorWithName(monitorName);
        if (tripMonitor != null) {
            final List<ChannelMonitor> channelMonitors = tripMonitor.getChannelMonitors();
            final Vector channelInfo = new Vector(channelMonitors.size());
            for (final ChannelMonitor channelMonitor : channelMonitors) {
                final Map record = new Hashtable();
                record.put(PV_KEY, channelMonitor.getPV());
                record.put(CHANNEL_CONNECTION_KEY, channelMonitor.isConnected());
                channelInfo.add(record);
            }
            return channelInfo;
        } else {
            return new Vector();
        }
    }
