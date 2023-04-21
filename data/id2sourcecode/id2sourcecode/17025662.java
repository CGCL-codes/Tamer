    public LoggerChannel[] getChannels() {
        LoggerChannel[] res = new LoggerChannel[channels.size()];
        channels.toArray(res);
        return (res);
    }
