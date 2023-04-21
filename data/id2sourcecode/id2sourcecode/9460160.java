    public void initialize() {
        if (thread != null) throw new IllegalStateException("Kernel already initialized.");
        writer = Executors.newFixedThreadPool(2, new NamedThreadFactory(toString() + "-writer"));
        thread = createHostThread();
        try {
            thread.connect();
            thread.start();
        } catch (IOException e) {
            throw new KernelException("Error hosting:" + address, e);
        }
    }
