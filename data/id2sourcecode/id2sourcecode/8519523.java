    protected void receiveKick(IRCParser p) {
        if (connectionStatus <= 1) {
            sendPrefix(Integer.toString(IRCUtil.ERR_NOTREGISTERED) + " :You have not registered");
        } else if (p.getParameterCount() < 2) {
            sendPrefix(Integer.toString(IRCUtil.ERR_NEEDMOREPARAMS) + " :Not enough parameters");
        } else {
            String channel = p.getParameter(1);
            if (!channel.equalsIgnoreCase(server.getChannel())) {
                sendPrefix(Integer.toString(IRCUtil.ERR_NOSUCHCHANNEL) + " " + channel + " :No such channel");
            } else if (connectionStatus == 2) {
                sendPrefix(Integer.toString(IRCUtil.ERR_NOTONCHANNEL) + " " + channel + " :You're not on that channel");
            } else {
                String nick = p.getParameter(2);
                if (!server.nickInChannel(nick)) {
                    sendPrefix(Integer.toString(IRCUtil.ERR_USERNOTINCHANNEL) + " " + nick + " " + channel + " :They aren't on that channel");
                } else if (!server.isOperator(this.nick)) {
                    sendPrefix(Integer.toString(IRCUtil.ERR_CHANOPRIVSNEEDED) + " " + channel + " :You're not channel operator");
                } else {
                    server.part(nick);
                    server.send(null, prefix() + p.getLine());
                }
            }
        }
    }
