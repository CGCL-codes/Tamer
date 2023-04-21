    public void broadcastMessage(Message message) {
        for (ChatClient client : clients.values()) {
            if (client.isInChannel(message.getChannel())) sendMessage(client, message);
        }
    }
