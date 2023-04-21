    @Test
    @IntegrationTest
    public void testJoinLeavePerformance() throws Exception {
        final String channelName = "perf";
        createChannel(channelName);
        String user = "dummy";
        DummyClient client = new DummyClient(user);
        client.connect(port).login();
        final String sessionKey = user;
        isPerformanceTest = true;
        int numIterations = 100;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numIterations; i++) {
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    Channel channel = channelService.getChannel(channelName);
                    DataManager dataManager = AppContext.getDataManager();
                    ClientSession session = (ClientSession) dataManager.getBinding(sessionKey);
                    channel.join(session);
                    channel.leave(session);
                }
            }, taskOwner);
        }
        long endTime = System.currentTimeMillis();
        System.err.println("join/leave, iterations: " + numIterations + ", elapsed time: " + (endTime - startTime) + " ms.");
    }
