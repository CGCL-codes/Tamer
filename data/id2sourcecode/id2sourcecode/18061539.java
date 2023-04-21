    @Test
    public void testRelocateWithLoginPreemptedAfterRelocate() throws Exception {
        String newNodeHost = "newNode";
        allowNewLogin = true;
        DummyClient client = createClientToRelocate(newNodeHost);
        DummyClient otherClient = createDummyClient("foo");
        SgsTestNode newNode = additionalNodes.get(newNodeHost);
        try {
            int newPort = newNode.getAppPort();
            client.relocate(newPort, true, true);
            assertTrue(otherClient.connect(newPort).login());
            checkBindings(1);
            client.assertDisconnectedCallbackInvoked(false);
        } finally {
            client.disconnect();
        }
    }
