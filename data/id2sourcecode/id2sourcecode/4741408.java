    public static void initialize(LaunchInfo launchInfo) {
        if (launchInfo == null) {
            throw new IllegalArgumentException("launchInfo must not be null!");
        }
        if (delayedReinitThread != null) {
            delayedReinitThread.cancel(true);
        }
        if (!isPlaying()) {
            if (launchInfo instanceof DirectPlayLaunchInfo) {
                launchHandler = new JDPlayLaunchHandler();
            } else if (launchInfo instanceof ParameterLaunchInfo) {
                launchHandler = new ParameterLaunchHandler();
            } else if (launchInfo instanceof DosboxLaunchInfo) {
                launchHandler = new DosboxLaunchHandler();
            }
            TempGameSettings.initalizeGameSettings(launchInfo.getRoomData().getChannel(), launchInfo.getRoomData().getModName());
            synchronized (launchHandler) {
                isInitialized = launchHandler.initialize(launchInfo);
                if (TabOrganizer.getRoomPanel() != null) {
                    int numSettings = GameDatabase.getGameSettings(launchInfo.getRoomData().getChannel(), launchInfo.getRoomData().getModName()).size();
                    if (isInitialized && numSettings == 0) {
                        TabOrganizer.getRoomPanel().initDone();
                    } else {
                        TabOrganizer.getRoomPanel().initDoneReadyDisabled();
                    }
                }
            }
            if (launchInfo instanceof ParameterLaunchInfo || launchInfo instanceof DosboxLaunchInfo) {
                if (!launchInfo.getRoomData().isInstant() && TabOrganizer.getRoomPanel() != null && GameDatabase.getGameSettings(launchInfo.getRoomData().getChannel(), launchInfo.getRoomData().getModName()).size() > 0) {
                    FrameOrganizer.openGameSettingsFrame(launchInfo.getRoomData());
                }
            }
        } else {
            determineInitializeActionWhenAlreadyPlaying(launchInfo);
        }
    }
