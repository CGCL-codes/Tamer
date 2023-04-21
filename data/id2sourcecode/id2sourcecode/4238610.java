    private void testReactor(String payload, int clients, int requests, int threads, int port) throws Exception {
        BlockingQueue<Worker> done = new LinkedBlockingQueue<Worker>();
        BlockingQueue<SocketChannel> ready = new LinkedBlockingQueue<SocketChannel>();
        CountDownLatch latch = new CountDownLatch(1);
        Server server = new Server(latch, ready, port);
        Executor executor = Executors.newFixedThreadPool(10);
        Reactor reactor = new ExecutorReactor(executor, 1);
        long start = System.currentTimeMillis();
        for (int i = 0; i < clients; i++) {
            new Client(latch, payload, port, requests);
        }
        for (int i = 0; i < clients; i++) {
            SocketChannel channel = ready.take();
            Worker worker = new Worker(done, reactor, channel, payload, i);
            reactor.process(worker);
        }
        int total = 0;
        for (int i = 0; i < clients; i++) {
            Worker worker = done.take();
            int accumulate = worker.getAccumulate();
            total += accumulate;
            System.err.println("Accumulated [" + accumulate + "] of [" + (requests * payload.length()) + "] closed [" + worker.getChannel().socket().isClosed() + "]");
        }
        System.err.println("Accumulated [" + total + "] of [" + (clients * requests * payload.length()) + "]");
        System.err.println("Total time to process [" + (clients * requests) + "] payloads from [" + clients + "] clients took [" + (System.currentTimeMillis() - start) + "]");
    }
