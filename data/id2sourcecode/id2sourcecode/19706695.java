    public ThroughputMonitor(Channel channel, long writeLimit, long readLimit) {
        this.name = "DEFAULT";
        this.changeConfiguration(channel, writeLimit, readLimit, DEFAULT_DELAY);
    }
