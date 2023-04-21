    public void testDefaultGw() throws Exception {
        final BaseCOContainer container = new BaseCOContainer();
        coos = COOSFactory.createCOOS(container.getResource("/simpleLS/coos.xml"), container);
        coos.start();
        System.out.println("Coos started");
        Thread.sleep(1000);
        coos2 = COOSFactory.createCOOS(container.getResource("/simpleLS/coos2.xml"), container);
        coos2.start();
        System.out.println("Coos2 started");
        coos3 = COOSFactory.createCOOS(container.getResource("/simpleLS/coos3.xml"), container);
        coos3.start();
        System.out.println("Coos3 started");
        coos4 = COOSFactory.createCOOS(container.getResource("/simpleLS/coos4.xml"), container);
        coos4.start();
        System.out.println("Coos4 started");
        coos.getRouter().setLoggingEnabled(false);
        coos2.getRouter().setLoggingEnabled(false);
        coos3.getRouter().setLoggingEnabled(false);
        coos4.getRouter().setLoggingEnabled(true);
        Plugin[] plugins = PluginFactory.createPlugins(container.getResource("/simpleLS/plugin.xml"), container);
        Thread.sleep(1000);
        for (int i = 0; i < plugins.length; i++) {
            plugins[i].connect();
        }
        Thread.sleep(4000);
        SimpleProducer producer = (SimpleProducer) plugins[4].getEndpoint().createProducer();
        DefaultMessage msg = new DefaultMessage();
        msg.setHeader("WAIT_TIME", "2000");
        Exchange exchange = producer.requestMessage("coos://UUID-ep1/foo/bar", msg);
        assertNotNull(exchange.getInBoundMessage());
        plugins[4].disconnect();
        ((Channel) plugins[4].getChannels().firstElement()).getTransport().setProperty("COOSInstanceName", "coos3");
        plugins[4].connect();
        SimpleConsumer consumer = (SimpleConsumer) plugins[4].getEndpoint().createConsumer();
        int i = 0;
        while (!consumer.getLastMsg().getName().equals("ReturnMsg") && i < 4) {
            Thread.sleep(1000);
            System.out.println("Waiting for msg");
            i++;
        }
        assertEquals("ReturnMsg", consumer.getLastMsg().getName());
        coos3.stop();
        coos4.stop();
        Thread.sleep(1500);
        System.out.println("testRequestMessage finished");
        System.out.println();
    }
