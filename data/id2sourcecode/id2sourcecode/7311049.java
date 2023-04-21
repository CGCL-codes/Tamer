    private Message handleDisconnectMessage(CommandMessage message) {
        Channel client = getChannel((String) message.getClientId());
        if (client == null) return handleUnknownClientMessage(message);
        removeChannel(client.getId());
        AcknowledgeMessage reply = new AcknowledgeMessage(message);
        reply.setDestination(message.getDestination());
        reply.setClientId(client.getId());
        return reply;
    }
