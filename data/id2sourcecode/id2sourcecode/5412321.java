    public ResponseTimeMonitor(Channel aResponseMonitoringChannel) {
        responseMonitoringChannel = aResponseMonitoringChannel;
        responseTime = 0;
        calculator = new ResponseTimeCalculator(90);
        calculationLimit = 100;
        throughputChannel = ChannelFactory.instance().getChannel();
        lastThroughputTime = 0;
        responseTimeListeners = new LinkedList();
    }
