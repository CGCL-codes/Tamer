    public void run() {
        while (true) {
            try {
                logger.fine("Waiting to get a ResponseTimeMonitorEvent.");
                ResponseTimeMonitorEvent event = (ResponseTimeMonitorEvent) channel.take();
                logger.fine("Taken a ResponseTimeMonitorEvent.");
                Channel channel = event.getChannel();
                int size = channel.size();
                ThroughputEvent[] events = new ThroughputEvent[size];
                for (int i = 0; i < size; i++) {
                    events[i] = (ThroughputEvent) channel.take();
                }
                ResponseTimeCalculator average = event.getMonitor().getResponseTimeCalculator();
                average.updateThroughputTime(events);
                long throughput = average.getResponseTime();
                logger.fine("Average throughput time: " + throughput);
                event.getMonitor().responseTimeChanged(throughput);
            } catch (InterruptedException e) {
                logger.log(Level.INFO, "A problem occurred whilst trying to calculate the average throughput stats.", e);
            }
        }
    }
