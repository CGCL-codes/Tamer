    private DummyClient createClientAndReassignIdentity(String newNodeHost, boolean setWaitForSuspendMessages) throws Exception {
        final String name = "foo";
        addNodes(newNodeHost);
        DummyClient client = createDummyClient(name);
        if (setWaitForSuspendMessages) {
            client.setWaitForSuspendMessages();
        }
        assertTrue(client.connect(serverNode.getAppPort()).login());
        SgsTestNode newNode = additionalNodes.get(newNodeHost);
        System.err.println("reassigning identity:" + name + " from server node to host: " + newNodeHost);
        identityAssigner.moveIdentity(name, serverNode.getNodeId(), newNode.getNodeId());
        System.err.println("(done) reassigning identity");
        return client;
    }
