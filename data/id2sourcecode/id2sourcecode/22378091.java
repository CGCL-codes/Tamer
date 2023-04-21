    public static void createRoom(RoomData rd) {
        final String[] info = { GameDatabase.getIDofGame(rd.getChannel()), rd.getRoomName(), rd.getPassword(), String.valueOf(rd.getMaxPlayers()), String.valueOf(rd.isInstant()), encodeIPMap(Globals.getInterfaceIPMap()), String.valueOf(rd.getModIndex()), String.valueOf(rd.isDoSearch()) };
        new Message(ClientProtocolCommands.CREATE_ROOM, info);
    }
