    public static String getLaunchedGame() {
        if (launchedGameInfo == null) {
            return null;
        }
        return launchedGameInfo.getRoomData().getChannel();
    }
