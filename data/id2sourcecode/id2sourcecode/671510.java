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
