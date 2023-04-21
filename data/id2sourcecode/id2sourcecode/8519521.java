    protected void receivePart(IRCParser p) {
        if (connectionStatus <= 1) {
            sendPrefix(Integer.toString(IRCUtil.ERR_NOTREGISTERED) + " :You have not registered");
        } else if (p.getParameterCount() < 1) {
            sendPrefix(Integer.toString(IRCUtil.ERR_NEEDMOREPARAMS) + " :Not enough parameters");
        } else {
            String channel = p.getParameter(1);
            if (!channel.equalsIgnoreCase(server.getChannel())) {
                sendPrefix(Integer.toString(IRCUtil.ERR_NOSUCHCHANNEL) + " " + channel + " :No such channel");
            } else if (connectionStatus == 2) {
                sendPrefix(Integer.toString(IRCUtil.ERR_NOTONCHANNEL) + " " + channel + " :You're not on that channel");
            } else {
                server.part(this.nick);
                server.send(null, prefix() + p.getLine());
                disconnect();
            }
        }
    }
