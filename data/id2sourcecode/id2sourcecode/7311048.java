    private Message handleConnectMessage(CommandMessage message) {
        Channel client = getChannel((String) message.getClientId());
        if (client == null) return handleUnknownClientMessage(message);
        return null;
    }
