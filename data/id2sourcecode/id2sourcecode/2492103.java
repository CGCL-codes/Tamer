    @Test
    @IntegrationTest
    public void testSendFromClientSessionWithDelayedJoin() throws Exception {
        String user = "user";
        String channelName = "test";
        createChannel(channelName);
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
            releaseChannelServerMethodHeld(userNode);
            checkChannelMessagesReceived(client, channelName, 3);
        } finally {
            client.disconnect();
        }
    }
