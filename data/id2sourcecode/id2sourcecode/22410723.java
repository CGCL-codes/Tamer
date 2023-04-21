    public RoomPanel(RoomData roomData) {
        this.roomData = roomData;
        this.users = new SortedListModel();
        users.add(Globals.getThisPlayer_loginName());
        initComponents();
        if (Client.getHamachiAddress().length() <= 0) {
            cb_useHamachi.setVisible(false);
        } else if (roomData.getHamachiIP().length() > 0) {
            hamachiWasEnabled = true;
            cb_useHamachi.setToolTipText("<html>Don't use this unless you have connection issues!<br>If you really need to use this consult with the room host!<br>Both you and the host have to be connected to <br>the same hamachi network!Otherwise it won't work!");
        }
        if (roomData.isHost()) {
            popup = new PlayerListPopupMenu(PlayerListPopupMenu.HOST_MODE, lst_userList);
            cb_useHamachi.setVisible(false);
            Hotkeys.bindHotKey(Hotkeys.ACTION_LAUNCH);
        } else {
            popup = new PlayerListPopupMenu(PlayerListPopupMenu.GENERAL_MODE, lst_userList);
        }
        lst_userList.setComponentPopupMenu(popup);
        roomStatusListCR = new RoomPlayerStatusListCellRenderer();
        lst_userList.setCellRenderer(roomStatusListCR);
        lst_userList.setDragEnabled(true);
        lst_userList.setDropMode(DropMode.USE_SELECTION);
        lst_userList.setTransferHandler(new UserListFileDropHandler());
        tp_chatInput.addKeyListener(new ChatInputKeyListener(ChatInputKeyListener.ROOM_CHAT_MODE, roomData.getChannel()));
        tp_chatOutput.addMouseListener(new HyperlinkMouseListener());
        if (!roomData.isHost()) {
            convertToJoinPanel();
        }
        Colorizer.colorize(this);
        chat("", roomData.getRoomName(), ChatStyles.USER);
        chat("", "room://" + roomData.getRoomID(), ChatStyles.USER);
        prgbar_connecting.setVisible(false);
        decideGameSettingsButtonVisility();
    }
