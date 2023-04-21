    @Test
    @IntegrationTest
    public void testSendFromClientSessionWithDelayedLeave() throws Exception {
        String user = "user";
        String channelName = "test";
        createChannel(channelName);
        SgsTestNode userNode = addNode();
        DummyClient client = new DummyClient(user);
        client.connect(userNode.getAppPort()).login();
        try {
            joinUsers(channelName, user);
            client.assertJoinedChannel(channelName);
            client.sendChannelMessage(channelName, 0);
            checkChannelMessagesReceived(client, channelName, 1);
            holdChannelServerMethodToNode(userNode, "leave");
            leaveUsers(channelName, user);
            waitForHeldChannelServerMethodToNode(userNode);
            client.sendChannelMessage(channelName, 0);
            Thread.sleep(1000);
            releaseChannelServerMethodHeld(userNode);
            client.assertLeftChannel(channelName);
            assertNull(client.nextChannelMessage());
        } finally {
            client.disconnect();
        }
    }
