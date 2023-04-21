    public void launch() {
        if (Launcher.isPlaying()) {
            Protocol.launch();
            return;
        }
        if (Launcher.predictSuccessfulLaunch() == false) {
            return;
        }
        new Thread() {

            @Override
            public void run() {
                try {
                    Launcher.launch();
                    Protocol.gameClosed(roomData.getChannel());
                    btn_gameSettings.setEnabled(true);
                } catch (Exception e) {
                    ErrorHandler.handleException(e);
                }
            }
        }.start();
    }
