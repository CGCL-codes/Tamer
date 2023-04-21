    public static void closeRoomPanel() {
        if (roomPanel != null) {
            tabHolder.remove(roomPanel);
            int index = tabHolder.indexOfTab(roomPanel.getRoomData().getChannel());
            if (index != -1) {
                tabHolder.setSelectedIndex(index);
            }
            if (roomPanel.isHost()) {
                Hotkeys.unbindHotKey(Hotkeys.ACTION_LAUNCH);
            }
            roomPanel = null;
            if (!Launcher.isPlaying()) {
                Launcher.deInitialize();
            }
            Globals.closeGameSettingsFrame();
            for (ChannelPanel cp : channelPanels) {
                cp.enableButtons();
            }
        }
    }
