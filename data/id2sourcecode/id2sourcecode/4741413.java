    public static void launch() {
        if (isInitialized()) {
            for (int i = 0; TabOrganizer.getChannelPanel(i) != null; i++) {
                TabOrganizer.getChannelPanel(i).disableButtons();
            }
            synchronized (launchHandler) {
                launchedGameInfo = launchHandler.getLaunchInfo();
                if (Settings.getSleepEnabled()) {
                    Globals.setSleepModeStatus(true);
                }
                boolean launchResult = launchHandler.launch();
                Globals.setSleepModeStatus(false);
                boolean doPrint = true;
                if (launchHandler instanceof JDPlayLaunchHandler) {
                    JDPlayLaunchHandler handler = (JDPlayLaunchHandler) launchHandler;
                    if (handler.isSearchAborted()) {
                        handler.resetAbortSearchFlag();
                        doPrint = false;
                    }
                }
                if (doPrint) {
                    if (!launchResult) {
                        FrameOrganizer.getClientFrame().printSystemMessage("Launch failed, maybe the game is not setup properly or a process closed unexpectedly!", false);
                    } else {
                        FrameOrganizer.getClientFrame().printSystemMessage("Game closed.", false);
                    }
                }
                launchedGameInfo = null;
                for (int i = 0; TabOrganizer.getChannelPanel(i) != null; i++) {
                    TabOrganizer.getChannelPanel(i).enableButtons();
                }
            }
        } else {
            throw new IllegalStateException("The game has to be initialized before launching it!");
        }
    }
