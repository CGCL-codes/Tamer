    public Acceptor(String name, ServerSocketChannel ssc, ConnectionFactory factory, int readThreads, int writeThreads, boolean enableWorkers, int workerThreads, int bufferCount, int readTries, int writeTries, boolean debugEnabled) throws IOException {
        super(name);
        this.ssc = ssc;
        this.factory = factory;
        this.gate = new Object();
        this.workersEnabled = enableWorkers;
        this.workerThreads = workerThreads;
        this.bufferCount = bufferCount;
        this.debugEnabled = debugEnabled;
        init(readThreads, writeThreads, readTries, writeTries);
    }
