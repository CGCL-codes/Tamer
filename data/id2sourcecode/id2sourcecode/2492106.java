    @Test
    @IntegrationTest
    public void testSendFromClientSessionWithDelayedJoinLeaveJoin() throws Exception {
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
            leaveUsers(channelName, user);
            joinUsers(channelName, user);
            for (int i = 0; i < 2; i++) {
                client.sendChannelMessage(channelName, i);
            }
            Thread.sleep(1000);
            releaseChannelServerMethodHeld(userNode);
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
