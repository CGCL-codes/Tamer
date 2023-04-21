    public void testPluginConnection() throws Exception {
        coos = COOSFactory.createCOOS(this.getClass().getResourceAsStream("/coos2.xml"), null);
        coos.start();
        System.out.println("Coos started");
        Plugin[] plugins = PluginFactory.createPlugins(this.getClass().getResourceAsStream("/plugin4Robust.xml"));
        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            try {
                plugin.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Exchange exchange = ((SimpleProducer) plugins[0].getEndpoint().createProducer()).sendMessageRobust("coos://segment2.UUID-ep2/foo/bar", new DefaultMessage());
        assertNull(exchange.getInBoundMessage());
        assertNull(exchange.getFaultMessage());
        plugins[0].disconnect((Channel) plugins[0].getChannels().firstElement());
        exchange = ((SimpleProducer) plugins[0].getEndpoint().createProducer()).sendMessageRobust("coos://segment2.UUID-ep2/foo/bar", new DefaultMessage());
        assertNull(exchange.getInBoundMessage());
        assertNotNull(exchange.getFaultMessage());
        System.out.println("testRequestMessage finished");
        System.out.println();
    }
