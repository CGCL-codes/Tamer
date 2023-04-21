    @Test
    @IntegrationTest
    public void testChannelClose() throws Exception {
        final String channelName = "closeTest";
        int count = getObjectCount();
        createChannel(channelName);
        printServiceBindings("after channel create");
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                Channel channel = getChannel(channelName);
                dataService.removeObject(channel);
            }
        }, taskOwner);
        Thread.sleep(100);
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                Channel channel = getChannel(channelName);
                if (getChannel(channelName) != null) {
                    fail("obtained closed channel");
                }
            }
        }, taskOwner);
        printServiceBindings("after channel close");
        assertEquals(count, getObjectCount());
    }
