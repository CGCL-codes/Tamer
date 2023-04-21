    public void launch(String... args) throws Exception {
        int port = Integer.parseInt(args[0]);
        int pause = Integer.parseInt(args[1]);
        boolean writeToFile = Boolean.parseBoolean(args[2]);
        boolean sycnFlush = true;
        if (args.length > 3) {
            sycnFlush = Boolean.parseBoolean(args[3]);
        }
        System.out.println("running echo server");
        System.out.println("\r\n---------------------------------------");
        System.out.println(ConnectionUtils.getImplementationVersion() + "  (" + ConnectionUtils.getImplementationDate() + ")");
        IServer server = null;
        if (args.length > 4) {
            int workers = Integer.parseInt(args[4]);
            server = new Server(port, new EchoLineHandler(pause, sycnFlush, writeToFile), 150);
            server.setWorkerpool(Executors.newFixedThreadPool(workers));
            System.out.println("mode threaded (pool size " + ((ThreadPoolExecutor) server.getWorkerpool()).getMaximumPoolSize() + ")");
        } else {
            server = new Server(port, new NonThreadedEchoLineHandler(pause, sycnFlush, writeToFile), 150);
            System.out.println("mode                      non threaded");
            System.out.println("synch flush               " + sycnFlush);
        }
        server.setFlushMode(FlushMode.ASYNC);
        System.out.println("synch flush               " + sycnFlush);
        System.out.println("pause                     " + pause);
        System.out.println("write to file             " + writeToFile);
        System.out.println("---------------------------------------\r\n");
        ConnectionUtils.registerMBean(server);
        System.out.println("running xsocket echo server");
        server.run();
    }
