    protected void receiveJoin(IRCParser p) {
        if (connectionStatus <= 1) {
            sendPrefix(Integer.toString(IRCUtil.ERR_NOTREGISTERED) + " :You have not registered");
        } else if (p.getParameterCount() < 1) {
            sendPrefix(Integer.toString(IRCUtil.ERR_NEEDMOREPARAMS) + " :Not enough parameters");
        } else if (connectionStatus >= 3) {
            String channel = p.getParameter(1);
            if (channel.equals("0")) {
                server.part(this.nick);
                server.send(null, prefix() + "PART " + server.getChannel());
                disconnect();
            } else if (!channel.equalsIgnoreCase(server.getChannel())) {
                sendPrefix(Integer.toString(IRCUtil.ERR_TOOMANYCHANNELS) + " " + channel + " :You have joined too many channels");
            }
        } else {
            String channel = p.getParameter(1);
            if (!channel.equalsIgnoreCase(server.getChannel())) {
                sendPrefix(Integer.toString(IRCUtil.ERR_NOSUCHCHANNEL) + " " + channel + " :No such channel");
                disconnect("Incorrect channel");
            } else {
                joinTimeoutTask.cancel();
                server.join(this.nick);
                server.send(null, prefix() + p.getLine());
                sendPrefix(Integer.toString(IRCUtil.RPL_NAMREPLY) + " " + nick + " = " + channel + " :" + server.getNickList());
                sendPrefix(Integer.toString(IRCUtil.RPL_ENDOFNAMES) + " " + nick + " " + channel + " :End of NAMES list");
                if (!server.getTopic().equals("")) {
                    sendPrefix(Integer.toString(IRCUtil.RPL_TOPIC) + " " + nick + " " + channel + " :" + server.getTopic());
                    sendPrefix(Integer.toString(IRCUtil.RPL_TOPICINFO) + " " + nick + " " + channel + " " + server.getTopicNick() + " :" + Long.toString(server.getTopicDate().getTime()));
                }
                connectionStatus = 3;
            }
        }
    }
