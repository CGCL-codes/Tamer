    public MainWindow() {
        connectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                if (XDCCConnectionManager.bot != null && XDCCConnectionManager.bot.isConnected()) {
                    DialogBuilder.showErrorDialog("Error", "Already Connected");
                    return;
                }
                Thread connectThread = new Thread() {

                    public void run() {
                        connectToServer();
                    }
                };
                connectThread.start();
            }
        });
        disconnectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                if (XDCCConnectionManager.bot != null && XDCCConnectionManager.bot.isConnected()) {
                    XDCCConnectionManager.bot.disconnect();
                    XDCCConnectionManager.bot = null;
                } else {
                    DialogBuilder.showErrorDialog("Error", "Must be connected to server & channel");
                }
            }
        });
        addToQueueButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                if (XDCCConnectionManager.bot != null && XDCCConnectionManager.bot.isConnected()) {
                    JFrame frame = new JFrame("XDCC Manager - Add To Queue");
                    frame.setContentPane(new AddToQueue(frame, XDCCConnectionManager.bot.getUsers(XDCCConnectionManager.bot.getChannels()[0])).mainPanel);
                    frame.pack();
                    frame.setVisible(true);
                } else {
                    DialogBuilder.showErrorDialog("Error", "Must be connected to server & channel");
                }
            }
        });
        viewStatusButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                if (XDCCConnectionManager.bot != null && XDCCConnectionManager.bot.isConnected()) {
                    JFrame frame = new JFrame("XDCC Manager - Current Status");
                    frame.setContentPane(new StatusWindow(frame).mainPanel);
                    frame.pack();
                    frame.setVisible(true);
                } else {
                    DialogBuilder.showErrorDialog("Error", "Must be connected to server & channel");
                }
            }
        });
        editQueueButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                if (XDCCConnectionManager.bot != null && XDCCConnectionManager.bot.isConnected()) {
                    JFrame frame = new JFrame("XDCC Manager - Edit Queue");
                    frame.setContentPane(new EditQueue(frame).mainPanel);
                    frame.pack();
                    frame.setVisible(true);
                } else {
                    DialogBuilder.showErrorDialog("Error", "Must be connected to server & channel");
                }
            }
        });
        sendMessageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                if (XDCCConnectionManager.bot != null && XDCCConnectionManager.bot.isConnected()) {
                    JFrame frame = new JFrame("XDCC Manager - Send Message");
                    frame.setContentPane(new SendMessage(frame).mainPanel);
                    frame.pack();
                    frame.setVisible(true);
                } else {
                    DialogBuilder.showErrorDialog("Error", "Must be connected to server & channel");
                }
            }
        });
    }
