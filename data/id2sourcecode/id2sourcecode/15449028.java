    private void registerClients() throws IOException {
        SelectionKey selKey;
        for (RegisterableClient client : registerableClients) {
            logger.debug("registering client " + client.getClientId() + " for writing");
            registerableClients.remove(client);
            selKey = client.getChannel().register(selector, SelectionKey.OP_WRITE);
            selKey.attach(client.getClientId());
        }
    }
