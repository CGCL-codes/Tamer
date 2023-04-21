    public DirectPlayLaunchInfo(RoomData roomData) {
        super(roomData);
        this.gameGUID = GameDatabase.getGuid(roomData.getChannel(), roomData.getModName());
    }
