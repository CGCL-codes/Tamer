        public void actionPerformed(ActionEvent actionEvent) {
            JMenuItem source = (JMenuItem) actionEvent.getSource();
            String kbCommand = source.getText();
            ServerListener chan = user.getChannel();
            StringBuffer commandBuf = new StringBuffer(32);
            if (kbCommand.equals("Kick")) {
                commandBuf.append("/kick ").append(chan.getName());
                commandBuf.append(" ").append(user.getNick());
            } else if (kbCommand.equals("Ban")) {
                commandBuf.append("/mode ").append(chan.getName());
                commandBuf.append(" +b *!*@*.");
            }
            chan.getServer().write(IRCMessageFactory.createClientMessage(commandBuf.toString(), chan.getName()));
        }
