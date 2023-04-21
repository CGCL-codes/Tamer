    public ThroughputMonitor(Channel channel, String name, long writeLimit, long readLimit, long delay) {
        this.name = name;
        this.changeConfiguration(channel, writeLimit, readLimit, delay);
    }
