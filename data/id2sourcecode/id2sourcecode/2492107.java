    @Test
    @IntegrationTest
    public void testSendFromClientSessionWithDelayedJoinAndCoordinatorCrash() throws Exception {
        String user = "user";
        String channelName = "test";
        SgsTestNode coordinatorNode = addNode();
        createChannel(channelName, null, coordinatorNode);
        SgsTestNode userNode = addNode();
        DummyClient client = new DummyClient(user);
        client.connect(userNode.getAppPort()).login();
        try {
            holdChannelServerMethodToNode(userNode, "join");
            joinUsers(channelName, user);
            waitForHeldChannelServerMethodToNode(userNode);
            for (int i = 0; i < 3; i++) {
                client.sendChannelMessage(channelName, i);
            }
            Thread.sleep(1000);
            coordinatorNode.shutdown(false);
            Thread.sleep(3000);
            checkChannelMessagesReceived(client, channelName, 3);
        } finally {
            client.disconnect();
        }
    }
