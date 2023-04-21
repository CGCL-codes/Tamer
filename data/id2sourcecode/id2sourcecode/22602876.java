    public RoomPanel(RoomData theRoomData) {
        this.roomData = theRoomData;
        this.users = new SortedListModel();
        users.add(Globals.getThisPlayerLoginName());
        initComponents();
        scrl_chatOutput.updateStyle();
        cmb_interface.setToolTipText("<html>Don't use this unless you have connection issues!" + "<br>If you really need to use this, consult with the room host!" + "<br>Both you and the host have to be connected to the same VPN network!" + "<br>Otherwise it won't work!");
        cmb_interface.setVisible(false);
        lbl_interface.setVisible(false);
        if (roomData.isHost()) {
            popup = new PlayerListPopupMenu(true, lst_userList);
            Hotkeys.bindHotKey(Hotkeys.ACTION_LAUNCH);
        } else {
            popup = new PlayerListPopupMenu(false, lst_userList);
        }
        lst_userList.setComponentPopupMenu(popup);
        roomStatusListCR = new RoomPlayerStatusListCellRenderer();
        lst_userList.setCellRenderer(roomStatusListCR);
        lst_userList.setDragEnabled(true);
        lst_userList.setDropMode(DropMode.USE_SELECTION);
        lst_userList.setTransferHandler(new UserListFileDropHandler());
        tp_chatInput.addKeyListener(new ChatInputKeyListener(ChatInputKeyListener.ROOM_CHAT_MODE, roomData.getChannel()));
        scrl_chatOutput.getTextPane().addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!evt.isControlDown()) {
                    tp_chatInput.setText(tp_chatInput.getText() + c);
                    tp_chatInput.requestFocusInWindow();
                    scrl_chatOutput.getTextPane().setSelectionStart(scrl_chatOutput.getTextPane().getDocument().getLength());
                    scrl_chatOutput.getTextPane().setSelectionEnd(scrl_chatOutput.getTextPane().getDocument().getLength());
                }
            }
        });
        if (!theRoomData.isHost()) {
            convertToJoinPanel();
        }
        Colorizer.colorize(this);
        chat("", theRoomData.getRoomName(), ChatStyles.USER);
        String channelID = GameDatabase.getShortName(theRoomData.getChannel()).replaceAll(" ", "_");
        chat("", "room://" + channelID + "/" + theRoomData.getRoomID(), ChatStyles.USER);
        prgbar_connecting.setVisible(false);
        decideGameSettingsButtonVisility();
    }
