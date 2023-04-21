    GameWindow(Settings settings, Player[] players, EncryptedMessageReader reader, EncryptedMessageWriter writer) {
        this.writer = writer;
        writer.setExceptionHandler(this);
        this.reader = reader;
        initComponents();
        initPokerTablePanel();
        addWindowListener(new GameWindowListener());
        getRootPane().setDefaultButton(sendBtn);
        this.settings = settings;
        if (settings.isObserving()) {
            holeCardLabel1.setVisible(false);
            holeCardLabel2.setVisible(false);
        }
        for (Player player : players) {
            tablePanel.addPlayer(player);
        }
        actionBtns.add(actionBtn1);
        actionBtns.add(actionBtn2);
        actionBtns.add(actionBtn3);
        swingSafe.clearBtns();
        holeCardLabels.add(holeCardLabel1);
        holeCardLabels.add(holeCardLabel2);
        swingSafe.clearCards();
        handType.setText("");
        potLabel.setText("...");
        statusLabel.setText("Waiting...");
        try {
            serverReaderThread = new ServerReaderThread();
            serverReaderThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "There was a problem connecting to the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        setVisible(true);
    }
