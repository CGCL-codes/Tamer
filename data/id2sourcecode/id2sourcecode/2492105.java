    @Test
    @IntegrationTest
    public void testSendFromClientSessionWithDelayedJoinLeave() throws Exception {
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
            client.sendChannelMessage(channelName, 0);
            releaseChannelServerMethodHeld(userNode);
            client.assertLeftChannel(channelName);
            Thread.sleep(3000);
            assertNull(client.nextChannelMessage());
        } finally {
            client.disconnect();
        }
    }
