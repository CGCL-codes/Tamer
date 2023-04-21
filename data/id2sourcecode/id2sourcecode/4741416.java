    public static boolean initInstantLaunch(RoomData roomData) {
        FrameOrganizer.getClientFrame().printSystemMessage("Initializing game ...", false);
        LaunchInfo launchInfo;
        LaunchMethods method = GameDatabase.getLaunchMethod(roomData.getChannel(), roomData.getModName());
        if (method == LaunchMethods.PARAMETER) {
            launchInfo = new ParameterLaunchInfo(roomData);
        } else if (method == LaunchMethods.DOS) {
            launchInfo = new DosboxLaunchInfo(roomData);
        } else {
            throw new IllegalArgumentException("You can't instantlaunch from " + method.toString() + " channel! GameName: " + roomData.getChannel() + " ModName: " + roomData.getModName());
        }
        if (isPlaying()) {
            boolean showJOptionPane = false;
            if (!roomData.getChannel().equals(launchedGameInfo.getRoomData().getChannel())) {
                showJOptionPane = true;
            } else if (!roomData.getIP().equals(launchedGameInfo.getRoomData().getIP())) {
                showJOptionPane = true;
            } else if (!(roomData.getModName() == null && launchedGameInfo.getRoomData().getModName() == null)) {
                if (!roomData.getModName().equals(launchedGameInfo.getRoomData().getModName())) {
                    showJOptionPane = true;
                }
            }
            if (showJOptionPane) {
                while (isPlaying()) {
                    int option = JOptionPane.showConfirmDialog(null, "<html>Coopnet has detected that the game \"<b>" + launchHandler.getLaunchInfo().getBinaryName() + "</b>\" is already running.<br>" + "You have to <b>close the game</b> to proceed launching.<br>" + "<br>" + "Press ok to retry or press cancel to abort the launch.", "WARNING: Another game is already running", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (option == JOptionPane.CANCEL_OPTION) {
                        return false;
                    }
                }
            }
        }
        Launcher.initialize(launchInfo);
        if (!Launcher.isInitialized()) {
            Protocol.closeRoom();
            Protocol.gameClosed(roomData.getChannel());
            TabOrganizer.getChannelPanel(roomData.getChannel()).enableButtons();
        }
        return Launcher.isInitialized();
    }
