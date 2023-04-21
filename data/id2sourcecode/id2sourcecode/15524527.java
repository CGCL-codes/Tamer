    @Override
    public void run() {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            InetSocketAddress isa;
            if ("*".equals(config.hostname)) {
                isa = new InetSocketAddress(config.port);
            } else {
                isa = new InetSocketAddress(config.hostname, config.port);
            }
            ssc.socket().bind(isa);
            acceptor = new Acceptor("server-" + config.name.replace(" ", "-").toLowerCase() + "-acceptor", ssc, config.factory, config.readThreads, config.writeThreads, config.enableWorkers, config.workerThreads, config.bufferCount, config.readTries, config.writeTries, config.debugEnabled);
            acceptor.setDaemon(false);
            acceptor.start();
            synchronized (acceptor) {
                try {
                    acceptor.wait();
                } catch (InterruptedException e) {
                }
            }
            synchronized (this) {
                running = true;
                notifyAll();
            }
            log.info("Started " + getClass().getSimpleName() + " " + config.name + " listening on " + config.hostname + ":" + config.port + " with " + config.readThreads + " readers (maximum retries: " + config.readTries + ") and " + config.writeThreads + " writers (maximum retries: " + config.writeTries + ")" + (config.enableWorkers ? ", with " + config.workerThreads + " worker threads using " + config.bufferCount + " buffers" : "") + (config.debugEnabled ? " (debug enabled)" : ""));
        } catch (IOException e) {
            log.error("Error starting server !", e);
            System.exit(1);
        }
        try {
            acceptor.join();
        } catch (InterruptedException e) {
        }
        log.debug(getName() + " stopped");
    }
