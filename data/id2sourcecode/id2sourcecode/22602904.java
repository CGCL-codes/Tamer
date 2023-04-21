            @Override
            public void run() {
                try {
                    Launcher.launch();
                    Protocol.gameClosed(roomData.getChannel());
                    btn_gameSettings.setEnabled(true);
                } catch (Exception e) {
                    ErrorHandler.handle(e);
                }
            }
