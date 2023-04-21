    public void testAddChannelNotStarted() {
        String id = "default-channel";
        destination.addChannel(id);
        boolean contains = destination.getChannels().contains(id);
        Assert.assertTrue(contains);
    }
