    @Test
    @IntegrationTest
    public void testSendWithCoordinatorCrash() throws Exception {
        String user = "user";
        String channelName = "test";
        SgsTestNode coordinatorNode = addNode();
        createChannel(channelName, null, coordinatorNode);
        SgsTestNode userNode = addNode();
        DummyClient client = new DummyClient(user);
        client.connect(userNode.getAppPort()).login();
        try {
            joinUsers(channelName, user);
            client.assertJoinedChannel(channelName);
            holdChannelServerMethodToNode(userNode, "send");
            for (int i = 0; i < 3; i++) {
                client.sendChannelMessage(channelName, i);
            }
            waitForHeldChannelServerMethodToNode(userNode);
            coordinatorNode.shutdown(false);
            Thread.sleep(5000);
            checkChannelMessagesReceived(client, channelName, 3);
        } finally {
            client.disconnect();
        }
    }
