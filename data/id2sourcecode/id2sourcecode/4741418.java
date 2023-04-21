    public static void instantLaunch(boolean launchClickedFromGameSettingsFrame) {
        if (Launcher.isInitialized()) {
            final String channel = launchHandler.getLaunchInfo().getRoomData().getChannel();
            TabOrganizer.getChannelPanel(channel).disableButtons();
            if (launchHandler.getLaunchInfo().getRoomData().isHost() && (isPlaying() || launchHandler.processExists())) {
                new Thread() {

                    @Override
                    public void run() {
                        String channelcopy = channel;
                        int ret = JOptionPane.showConfirmDialog(null, "<html>Coopnet has detected that the game \"<b>" + launchHandler.getLaunchInfo().getBinaryName() + "</b>\" is already running.<br>" + "Please make sure the other players can <b>connect to a running server</b> there<br>" + "or <b>close the game</b> before confirming this message.<br>" + "<br>" + "<br>If the game is still running after you have confirmed this message," + "<br>Coopnet will create the room without launching your game.", "WARNING: Game is already running", JOptionPane.OK_CANCEL_OPTION);
                        if (ret == JOptionPane.OK_OPTION) {
                            if (launchHandler.processExists()) {
                                Protocol.createRoom(launchHandler.getLaunchInfo().getRoomData());
                                while (launchHandler != null && launchHandler.processExists()) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException ex) {
                                    }
                                }
                                Protocol.gameClosed(channelcopy);
                            }
                        } else {
                            Launcher.deInitialize();
                            TabOrganizer.getChannelPanel(channelcopy).enableButtons();
                        }
                    }
                }.start();
            } else if (!launchHandler.getLaunchInfo().getRoomData().isHost() && !launchClickedFromGameSettingsFrame) {
                FrameOrganizer.openGameSettingsFrame(launchHandler.getLaunchInfo().getRoomData());
            } else {
                if (launchHandler.getLaunchInfo().getRoomData().isHost()) {
                    Protocol.createRoom(launchHandler.getLaunchInfo().getRoomData());
                    String mv = TempGameSettings.getGameSettingValue("map");
                    if (mv != null && mv.length() > 0) {
                        Protocol.sendSetting("map", TempGameSettings.getGameSettingValue("map"));
                    }
                    for (GameSetting gs : TempGameSettings.getGameSettings()) {
                        Protocol.sendSetting(gs.getName(), gs.getValue());
                    }
                }
                Launcher.launch();
                Launcher.deInitialize();
                Protocol.gameClosed(channel);
                TabOrganizer.getChannelPanel(channel).enableButtons();
            }
        }
    }
