    public void addListeners() {
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        textin.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processLine(textin.getText());
                }
            }
        });
        connect_btn.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (connect_btn.getLabel().equals("connect")) {
                    listener.connect();
                    listener.login();
                    if (listener.getConnected()) {
                        connect_btn.setLabel("disconnect");
                        disconnected = false;
                    }
                } else {
                    textout.append("\nDisconnecting..");
                    listener.disconnect();
                    if (!listener.getConnected()) {
                        connect_btn.setLabel("connect");
                        disconnected = true;
                        memberslist.removeAll();
                    }
                }
            }
        });
        leaveChannelBtn.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (!currentChannel.isConsole) {
                    listener.sendCommand(new IRCMessage(OJConstants.ID_CLIENT_PART, new String[] { currentChannel.channelName }));
                    leftChannel(currentChannel.channelName);
                }
            }
        });
        memberslist.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == e.SELECTED) {
                    if (currentChannel == null || currentChannel.isConsole) listener.sendCommand(new IRCMessage(OJConstants.ID_CLIENT_JOIN, new String[] { memberslist.getSelectedItem() }));
                }
            }
        });
        channelChoice.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == e.SELECTED) {
                    System.out.println(e.getItem());
                    changeChannel(e.getItem().toString());
                }
            }
        });
        viewChoice.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == e.SELECTED) {
                    System.out.println("v " + e.getItem());
                }
            }
        });
        configButton.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                showConfigPanel();
            }
        });
    }
