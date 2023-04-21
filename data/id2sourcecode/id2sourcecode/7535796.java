        public void actionPerformed(ActionEvent actionEvent) {
            JMenuItem source = (JMenuItem) actionEvent.getSource();
            String infoCommand = source.getText();
            ServerListener chan = user.getChannel();
            StringBuffer commandBuf = new StringBuffer(32);
            if (infoCommand.equals("Who")) {
                commandBuf.append("/who ").append(getNick());
            } else if (infoCommand.equals("Whois")) {
                commandBuf.append("/whois ").append(getNick());
            } else if (infoCommand.equals("DNS Lookup")) {
            } else if (infoCommand.equals("Trace")) {
                commandBuf.append("/trace ").append(getNick());
            } else if (infoCommand.equals("UserHost")) {
                commandBuf.append("/userhost ").append(getNick());
            }
            chan.getServer().write(IRCMessageFactory.createClientMessage(commandBuf.toString(), chan.getName()));
        }
