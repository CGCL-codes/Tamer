    @Test
    @IntegrationTest
    public void testSendFromClientSessionWithDelayedJoinLeaveAndCoordinatorCrash() throws Exception {
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
            client.sendChannelMessage(channelName, 0);
            Thread.sleep(1000);
            coordinatorNode.shutdown(false);
            Thread.sleep(3000);
            client.assertLeftChannel(channelName);
            assertNull(client.nextChannelMessage());
        } finally {
            client.disconnect();
        }
    }
