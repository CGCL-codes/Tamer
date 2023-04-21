    protected void receiveTopic(IRCParser p) {
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
                if (p.getParameterCount() < 2) {
                    if (server.getTopic().equals("")) {
                        sendPrefix(Integer.toString(IRCUtil.RPL_NOTOPIC) + " :No topic is set");
                    } else {
                        sendPrefix(Integer.toString(IRCUtil.RPL_TOPIC) + " " + nick + " " + channel + " :" + server.getTopic());
                        sendPrefix(Integer.toString(IRCUtil.RPL_TOPICINFO) + " " + nick + " " + channel + " " + server.getTopicNick() + " :" + Long.toString(server.getTopicDate().getTime()));
                    }
                } else {
                    server.setTopic(nick, p.getTrailing());
                    server.send(null, prefix() + p.getLine());
                }
            }
        }
    }
