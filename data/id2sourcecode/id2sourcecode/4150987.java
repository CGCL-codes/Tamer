    protected DummyClient createDummyClient(String name, SgsTestNode node) {
        DummyClient client = new DummyClient(name);
        client.connect(node.getAppPort());
        assertTrue(client.login());
        return client;
    }
