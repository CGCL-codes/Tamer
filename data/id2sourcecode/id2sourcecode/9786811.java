    private Button getChannelsButton() {
        Button button3 = new Button();
        button3.setLabel("channel list");
        button3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(final java.awt.event.ActionEvent e) {
                irc.listChannels();
            }
        });
        return button3;
    }
