    @Test
    public void testRelocateWithInterveningClientLoginRejected() throws Exception {
        String newNodeHost = "newNode";
        DummyClient client = createClientToRelocate(newNodeHost);
        DummyClient otherClient = createDummyClient("foo");
        SgsTestNode newNode = additionalNodes.get(newNodeHost);
        try {
            int newPort = newNode.getAppPort();
            assertFalse(otherClient.connect(newPort).login());
            client.relocate(newPort, true, true);
            checkBindings(1);
            client.assertDisconnectedCallbackNotInvoked();
        } finally {
            client.disconnect();
        }
    }
