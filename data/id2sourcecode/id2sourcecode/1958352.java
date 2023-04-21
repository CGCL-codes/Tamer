    public void testAddChannelStartedBrokerKnows() {
        start();
        String id = "default-channel";
        Map csMap = new HashMap();
        csMap.put(id, null);
        broker.setChannelSettings(csMap);
        destination.addChannel(id);
        boolean contains = destination.getChannels().contains(id);
        Assert.assertTrue(contains);
    }
