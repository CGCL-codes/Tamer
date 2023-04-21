    public void changeConfiguration(Channel channel, long writeLimit, long readLimit, long delayToSet) {
        this.limitWrite = writeLimit;
        this.limitRead = readLimit;
        this.delay = delayToSet;
        this.sleepingDelay = this.delay >> 4;
        if (this.sleepingDelay > 200) {
            this.sleepingDelay = 200;
        }
        if (this.sleepingDelay < 10) {
            this.sleepingDelay = 10;
        }
        this.setMonitoredChannel(channel);
    }
