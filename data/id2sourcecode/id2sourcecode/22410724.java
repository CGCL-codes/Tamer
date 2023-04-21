    private void decideGameSettingsButtonVisility() {
        if (Launcher.isPlaying()) {
            btn_gameSettings.setEnabled(false);
        }
        if (GameDatabase.getLocalSettingCount(roomData.getChannel(), roomData.getModName()) + GameDatabase.getServerSettingCount(roomData.getChannel(), roomData.getModName()) == 0) {
            btn_gameSettings.setVisible(false);
        }
    }
