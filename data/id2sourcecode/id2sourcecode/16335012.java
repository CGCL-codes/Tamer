    private void create(java.awt.event.ActionEvent evt) {
        passw = new String(pf_password.getPassword());
        modindex = cmb_mod.getSelectedIndex();
        if (modindex == 0) {
            modindex = -1;
        }
        btn_create.setEnabled(false);
        if (btn_create.getText().equals("Create")) {
            RoomData rd = new RoomData(true, channel, modindex, Globals.getInterfaceIPMap(), (Integer) spn_maxPlayers.getValue(), "", tf_name.getText(), 0l, passw, cb_searchEnabled.isSelected(), cb_instantroom.isSelected());
            Protocol.createRoom(rd);
            FrameOrganizer.closeRoomCreationFrame();
            Globals.setLastRoomName(tf_name.getText());
            TabOrganizer.getChannelPanel(channel).disableButtons();
        } else if (btn_create.getText().equals("Launch")) {
            FrameOrganizer.closeRoomCreationFrame();
            TabOrganizer.getChannelPanel(channel).disableButtons();
            new Thread() {

                @Override
                public void run() {
                    try {
                        RoomData rd = new RoomData(true, channel, modindex, Globals.getInterfaceIPMap(), (Integer) spn_maxPlayers.getValue(), "", tf_name.getText(), 0l, passw, cb_searchEnabled.isSelected(), cb_instantroom.isSelected());
                        boolean launch = Launcher.initInstantLaunch(rd);
                        if (launch) {
                            Launcher.instantLaunch();
                        }
                    } catch (Exception e) {
                        ErrorHandler.handle(e);
                    }
                }
            }.start();
        } else if (btn_create.getText().equals("Setup & Launch")) {
            FrameOrganizer.closeRoomCreationFrame();
            new Thread() {

                @Override
                public void run() {
                    try {
                        RoomData rd = new RoomData(true, channel, modindex, Globals.getInterfaceIPMap(), (Integer) spn_maxPlayers.getValue(), "", tf_name.getText(), 0l, passw, cb_searchEnabled.isSelected(), cb_instantroom.isSelected());
                        boolean launch = Launcher.initInstantLaunch(rd);
                        if (launch) {
                            FrameOrganizer.openGameSettingsFrame(rd);
                        }
                    } catch (Exception e) {
                        ErrorHandler.handle(e);
                    }
                }
            }.start();
        }
    }
