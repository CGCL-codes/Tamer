    public Server(ServerConfig config) {
        super("server-" + config.name.replace(" ", "-").toLowerCase());
        this.config = config;
        this.debugEnabled = config.debugEnabled;
        if (config.readThreads <= 0 || config.writeThreads <= 0 || (config.enableWorkers && config.workerThreads <= 0)) {
            if (config.enableWorkers) {
                throw new RuntimeException("You should at least use one reader thread, one writer thread and one worker thread");
            } else {
                throw new RuntimeException("You should at least use one write thread and one read thread");
            }
        }
    }
