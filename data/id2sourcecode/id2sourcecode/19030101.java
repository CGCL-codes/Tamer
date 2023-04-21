    private void decideVisibility() {
        if (roomData.isHost() && GameDatabase.getLocalSettingCount(roomData.getChannel(), roomData.getModName()) + GameDatabase.getServerSettingCount(roomData.getChannel(), roomData.getModName()) > 0) {
            if (!roomData.isInstant()) {
                btn_close.setEnabled(false);
            }
            setVisible(true);
        } else if (!roomData.isHost() && GameDatabase.getLocalSettingCount(roomData.getChannel(), roomData.getModName()) > 0) {
            if (!roomData.isInstant()) {
                btn_close.setEnabled(false);
            }
            setVisible(true);
        } else {
            if (!roomData.isInstant()) {
                TabOrganizer.getRoomPanel().initDone();
            } else {
                setVisible(true);
            }
        }
    }
