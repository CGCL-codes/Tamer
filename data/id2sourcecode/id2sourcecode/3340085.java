    @Override
    public void run() throws Exception {
        Lobby lobby = (Lobby) AppContext.getDataManager().getBinding(DarkstrisServer.LOBBY);
        long id = lobby.getNextRoomId();
        System.out.println("CreateAndJoinTask.run(room" + id + ", " + playerRef.get().getName() + ")");
        ServerRoom room = new ServerRoom(id, maxPlayers, playerRef.get());
        Channel lobbyChannel = AppContext.getChannelManager().getChannel(DarkstrisServer.LOBBY_CHANNEL);
        AppContext.getDataManager().setBinding("room" + id, room);
        lobbyChannel.send(null, Protocol.roomCreated(id, room.getMaxPlayers(), playerRef.get().getPlayerInfo()));
        AppContext.getTaskManager().scheduleTask(new RemoveFromLobbyTask(playerRef.get().getClientSession()));
        AppContext.getDataManager().markForUpdate(lobby);
        lobby.addRoom(room);
    }
