    public OutgoingThread(BufferedWriter writer) {
        super();
        this.queue = new LinkedBlockingQueue<DelayedMessage>();
        this.writer = writer;
        this.defaultDelay = 1000;
        this.addingLock = new Object();
        setDaemon(true);
    }
