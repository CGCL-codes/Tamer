        public void actionPerformed(ActionEvent actionEvent) {
            JMenuItem source = (JMenuItem) actionEvent.getSource();
            String modeCommand = source.getText();
            String nick = getNick();
            ServerListener chan = user.getChannel();
            StringBuffer commandBuf = new StringBuffer(32);
            if (modeCommand.equals("Give Voice")) {
                commandBuf.append("/mode ").append(chan.getName());
                commandBuf.append(" +v ").append(nick);
            } else if (modeCommand.equals("Take Voice")) {
                commandBuf.append("/mode ").append(chan.getName());
                commandBuf.append(" -v ").append(nick);
            } else if (modeCommand.equals("Give Ops")) {
                commandBuf.append("/mode ").append(chan.getName());
                commandBuf.append(" +o ").append(nick);
            } else if (modeCommand.equals("Take Ops")) {
                commandBuf.append("/mode ").append(chan.getName());
                commandBuf.append(" -o ").append(nick);
            }
            chan.getServer().write(IRCMessageFactory.createClientMessage(commandBuf.toString(), chan.getName()));
        }
