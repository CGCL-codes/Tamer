    public synchronized void logThroughputEvent(ThroughputEvent event) {
        try {
            throughputChannel.put(event);
            lastThroughputTime = System.currentTimeMillis();
            if (throughputChannel.size() >= calculationLimit) {
                responseMonitoringChannel.put(new ResponseTimeMonitorEvent(this, throughputChannel));
                throughputChannel = ChannelFactory.instance().getChannel();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
