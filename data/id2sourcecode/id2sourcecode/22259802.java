    public NamedChannelWithTimeout(RepNode repNode, SocketChannel channel, int timeoutMs) {
        super(channel);
        this.timeoutMs = timeoutMs;
        this.envImpl = repNode.getRepImpl();
        this.logger = repNode.getLogger();
        readActivity = true;
        if (timeoutMs > 0) {
            repNode.getChannelTimeoutTask().register(this);
        }
    }
