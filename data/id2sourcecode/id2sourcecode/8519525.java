    protected void receivePrivmsg(IRCParser p) {
        if (connectionStatus <= 1) {
            sendPrefix(Integer.toString(IRCUtil.ERR_NOTREGISTERED) + " :You have not registered");
        } else if (p.getParameterCount() < 2) {
            if (p.getLine().indexOf(" :") > -1) {
                sendPrefix(Integer.toString(IRCUtil.ERR_NORECIPIENT) + " :No recipient given (PRIVMSG)");
            } else {
                sendPrefix(Integer.toString(IRCUtil.ERR_NOTEXTTOSEND) + " :No text to send");
            }
        } else {
            String recipient = p.getParameter(1);
            if (recipient.equalsIgnoreCase(server.getChannel())) {
                if (connectionStatus == 2) {
                    sendPrefix(Integer.toString(IRCUtil.ERR_CANNOTSENDTOCHAN) + " " + recipient + " :Cannot send to channel");
                } else {
                    server.sendExcept(this.nick, prefix() + p.getLine());
                }
            } else if (!server.nickExists(recipient)) {
                sendPrefix(Integer.toString(IRCUtil.ERR_NOSUCHNICK) + " " + recipient + " :No such nick/channel");
            } else {
                server.send(recipient, prefix() + p.getLine());
            }
        }
    }
