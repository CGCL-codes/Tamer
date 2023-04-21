    public ThroughputMonitor(Channel channel, String name, long writeLimit, long readLimit) {
        this.name = name;
        this.changeConfiguration(channel, writeLimit, readLimit, DEFAULT_DELAY);
    }
