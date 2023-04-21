    @Test
    @IntegrationTest
    public void testSendFromClientSessionWithDelayedJoinLeaveJoinAndCoordinatorCrash() throws Exception {
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
            leaveUsers(channelName, user);
            joinUsers(channelName, user);
            for (int i = 0; i < 2; i++) {
                client.sendChannelMessage(channelName, i);
            }
            Thread.sleep(1000);
            coordinatorNode.shutdown(false);
            Thread.sleep(3000);
            client.assertJoinedChannel(channelName);
            checkChannelMessagesReceived(client, channelName, 2);
            for (int i = 0; i < 2; i++) {
                client.sendChannelMessage(channelName, i);
            }
            checkChannelMessagesReceived(client, channelName, 2);
        } finally {
            client.disconnect();
        }
    }
