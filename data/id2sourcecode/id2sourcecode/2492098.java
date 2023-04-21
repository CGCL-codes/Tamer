    @Test
    @IntegrationTest
    public void testChannelCloseWithMembers() throws Exception {
        final String channelName = "closeTest";
        String user = "user";
        DummyClient client = new DummyClient(user);
        client.connect(serverNode.getAppPort()).login();
        try {
            int count = getObjectCount();
            createChannel(channelName);
            printServiceBindings("after channel create");
            joinUsers(channelName, user);
            client.assertJoinedChannel(channelName);
            client.sendChannelMessage(channelName, 0);
            checkChannelMessagesReceived(client, channelName, 1);
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
            Thread.sleep(6000);
            printServiceBindings("after sleep");
            assertEquals(count, getObjectCount());
        } finally {
            client.disconnect();
        }
    }
