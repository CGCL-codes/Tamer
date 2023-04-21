    public void testGuaranteedDeliveryMultiplePaths() throws Exception {
        coos2 = COOSFactory.createCOOS(this.getClass().getResourceAsStream("/coos2GD.xml"), null);
        coos2.start();
        System.out.println("Coos2 started");
        coos = COOSFactory.createCOOS(this.getClass().getResourceAsStream("/coosGD.xml"), null);
        coos.start();
        System.out.println("Coos started");
        Plugin[] plugins = PluginFactory.createPlugins(this.getClass().getResourceAsStream("/plugin2.xml"));
        plugins[0].connect();
        plugins[1].connect();
        Thread.sleep(3000);
        final SimpleProducer producer = (SimpleProducer) plugins[0].getEndpoint().createProducer();
        Thread t = new Thread() {

            public void run() {
                try {
                    Thread.sleep(4000);
                    synchronized (GuaranteedDeliveryTest.this) {
                        coos.getTransport("TCP1").stop();
                        coos.getChannel("out").disconnect();
                        System.out.println("Stopping transport. Rounds up tp now: " + testrounds);
                        coos3 = COOSFactory.createCOOS(this.getClass().getResourceAsStream("/coos3GD.xml"), null);
                        coos3.getRouter().setLoggingEnabled(true);
                        coos.getRouter().setLoggingEnabled(true);
                        coos2.getRouter().setLoggingEnabled(true);
                        coos3.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Coos3 started");
            }
        };
        t.start();
        long testFinishTime = System.currentTimeMillis() + 20000;
        testrounds = 0;
        while (System.currentTimeMillis() < testFinishTime) {
            synchronized (this) {
                Message msg = new DefaultMessage();
                msg.setHeader(Message.ROBUST_DELIVERY_TIME, "10000");
                Exchange exchange = producer.requestMessage("coos://segment2.UUID-ep2/foo/bar", msg);
                if (exchange.getFaultMessage() != null) {
                    System.out.println(exchange.getFaultMessage().getHeader(Message.ERROR_REASON) + " after rounds: " + testrounds);
                    msg = new DefaultMessage();
                    msg.setHeader(Message.ROBUST_DELIVERY_TIME, "10000");
                    exchange = producer.requestMessage("coos://segment2.UUID-ep2/foo/bar", msg);
                }
                assertNotNull(exchange.getInBoundMessage());
                testrounds++;
            }
        }
        System.out.println("Rounds: " + testrounds);
        System.out.println("testGuaranteedDeliveryMultiplePaths finished");
        System.out.println();
    }
