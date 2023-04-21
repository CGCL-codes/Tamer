    private void unregisterClients() throws IOException {
        for (RegisterableClient client : unregisterableClients) {
            logger.debug("unregistering client " + client.getClientId() + " for writing");
            unregisterableClients.remove(client);
            client.getChannel().keyFor(selector).cancel();
            client.getChannel().close();
            selector.selectNow();
        }
    }
