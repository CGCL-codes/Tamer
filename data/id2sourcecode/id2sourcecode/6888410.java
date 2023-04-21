    @Override
    public void run() throws Exception {
        System.out.println("DropRoomTask.run(room" + id + ")");
        DataManager dataManager = AppContext.getDataManager();
        ServerRoom room = (ServerRoom) dataManager.getBinding("room" + id);
        Channel lobbyChannel = AppContext.getChannelManager().getChannel(DarkstrisServer.LOBBY_CHANNEL);
        dataManager.markForUpdate(room);
        lobbyChannel.send(null, Protocol.roomDropped(room.getId()));
        Lobby lobby = (Lobby) AppContext.getDataManager().getBinding(DarkstrisServer.LOBBY);
        lobby.removeRoom(room);
        room.getChannel().leaveAll();
        System.out.println("DropRoomTask -> removeChannel(room" + room.getId() + ");");
        dataManager.removeObject(room.getChannel());
        System.out.println("DropRoomTask -> removeObject(room" + room.getId() + ");");
        dataManager.removeBinding("room" + id);
        dataManager.removeObject(room);
    }
