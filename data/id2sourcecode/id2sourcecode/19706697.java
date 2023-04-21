    public ThroughputMonitor(Channel channel, long writeLimit, long readLimit, long delay) {
        this.name = "DEFAULT";
        this.changeConfiguration(channel, writeLimit, readLimit, delay);
    }
