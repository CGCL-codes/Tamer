    @Test
    @IntegrationTest
    public void testSendFromClientSessionWithDelayedLeaveAndCoordinatorCrash() throws Exception {
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
            client.sendChannelMessage(channelName, 0);
            checkChannelMessagesReceived(client, channelName, 1);
            holdChannelServerMethodToNode(userNode, "leave");
            leaveUsers(channelName, user);
            waitForHeldChannelServerMethodToNode(userNode);
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
