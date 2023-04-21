    public Main(String[] args) {
        try {
            conf = new Config(args);
            debug = conf.getDebug();
            Runtime.getRuntime().addShutdownHook(new ShutdownInterceptor());
            System.out.print("Start log writer thread... ");
            StatsWriter sw = SingletonFactory.getInstance(StatsWriter.class);
            writerThread = new Thread(sw);
            writerThread.setPriority(Thread.MIN_PRIORITY);
            writerThread.start();
            System.out.println("\t OK");
            while (conf.nextTest()) {
                System.out.println("Test: " + conf.getName());
                System.out.println("---------------------------------------");
                try {
                    WmsClient wmsClient = new WmsClient(conf.getUrl(), conf.getLogin(), conf.getPasswd(), conf.getVendor());
                    wmsClient.printCapabilities();
                    if (!wmsClient.checkLayers(conf.getLayers())) {
                        System.err.println("ERR: Selected Layer isn't available:");
                        continue;
                    }
                    if (!wmsClient.checkLayers(conf.getLayers(), conf.getSrs())) {
                        System.err.println("ERR: Selected Srs isn't available:");
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("WARN: GetCapabilities for test " + conf.getName() + " failed with: " + e.getMessage());
                    System.out.println("WARN: with URL: " + conf.getUrl());
                    continue;
                }
                try {
                    System.out.println("Start Multithread tester:");
                    AbstractWmsTester test;
                    String name = conf.getMode();
                    if (conf.getMode().equalsIgnoreCase("rer")) {
                        test = new RandomExtentRead();
                    } else if (conf.getMode().equalsIgnoreCase("zer")) {
                        test = new ZoomExtentRead();
                    } else if (conf.getMode().equalsIgnoreCase("rsr")) {
                        test = new RandomSizeRead();
                    } else {
                        test = new RandomSequenceRead();
                    }
                    test.setTestConf(conf);
                    mtt = createMultiThreadTest(test, conf.getThreads(), conf.getIncrease(), conf.getTotalTime());
                    mtt.start();
                    mtt.join();
                } catch (Exception e) {
                    System.out.println("WARN: test :" + conf.getName() + " Connection problem ?? : " + e.getMessage());
                }
            }
            shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
