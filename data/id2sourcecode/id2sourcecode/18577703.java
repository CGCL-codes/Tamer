    public ParameterLaunchInfo(RoomData roomData) {
        super(roomData);
        binaryPath = GameDatabase.getLaunchPathWithExe(roomData.getChannel(), roomData.getModName());
        if (roomData.isHost()) {
            parameters = " " + GameDatabase.getHostPattern(roomData.getChannel(), roomData.getModName());
        } else {
            parameters = " " + GameDatabase.getJoinPattern(roomData.getChannel(), roomData.getModName());
        }
    }
