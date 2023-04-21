    @Test
    @IntegrationTest
    public void testChannelLeaveAfterRelocate() throws Exception {
        String channelName = "foo";
        createChannel(channelName);
        DummyClient client = new DummyClient(REX);
        client.connect(port).login();
        SgsTestNode node1 = addNode();
        try {
            joinUsers(channelName, oneUser);
            checkUsersJoined(channelName, oneUser);
            sendMessagesToChannel(channelName, 2);
            checkChannelMessagesReceived(client, channelName, 2);
            moveClient(client, serverNode, node1);
            checkUsersJoined(channelName, oneUser);
            leaveUsers(channelName, oneUser);
            client.assertLeftChannel(channelName);
            checkUsersJoined(channelName, noUsers);
        } finally {
            client.disconnect();
        }
    }
