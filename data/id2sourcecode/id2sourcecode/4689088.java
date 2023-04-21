    @Override
    public void run() throws Exception {
        System.out.println("RemoveFromLobbyTask.run(" + sessRef.get().getName() + ")");
        Lobby lobby = (Lobby) AppContext.getDataManager().getBinding(DarkstrisServer.LOBBY);
        AppContext.getDataManager().markForUpdate(lobby);
        lobby.remove(sessRef.get().getName());
        Channel lobbyChannel = AppContext.getChannelManager().getChannel(DarkstrisServer.LOBBY_CHANNEL);
        lobbyChannel.leave(sessRef.get());
    }
