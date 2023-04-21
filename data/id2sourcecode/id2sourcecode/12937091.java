    public void playerLogout(int playerId) {
        ChatClient chatClient = players.get(playerId);
        if (chatClient != null) {
            players.remove(playerId);
            BroadcastService.getInstance().removeClient(chatClient);
            if (chatClient.getChannelHandler() != null) chatClient.getChannelHandler().close(); else log.warn("Received logout event without client authentication for player " + playerId);
        }
    }
