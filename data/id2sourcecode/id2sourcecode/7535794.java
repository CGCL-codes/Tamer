        public void actionPerformed(ActionEvent actionEvent) {
            JMenuItem source = (JMenuItem) actionEvent.getSource();
            String ctcpCommand = source.getText();
            String nick = getNick();
            user.getChannel().getServer().write(IRCMessageFactory.createCtcpClientMessage(nick, ctcpCommand));
        }
