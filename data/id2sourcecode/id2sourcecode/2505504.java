    public PluginResult handle(Bot bot, InCommand command) throws Exception {
        AuthenticationResponse response = matchResponse(bot, command);
        if (response != null) {
            String nick = response.getNick();
            UserAction action = actions.get(nick);
            boolean inChannel = false;
            for (Enumeration e = bot.getState().getChannel(bot.getChannelName()).getMembers(); e.hasMoreElements(); ) {
                Member member = (Member) e.nextElement();
                if (member.getNick().getNick().equals(nick)) {
                    inChannel = true;
                }
            }
            if (!inChannel) {
                bot.respond(nick, action.getResponseMode(), "You must be in the channel to " + action.getDescription());
            } else if (!response.isValid()) {
                bot.respond(nick, action.getResponseMode(), "You have to be registered with NickServ to " + action.getDescription());
            } else {
                try {
                    action.execute(bot, nick);
                } catch (UserRuntimeException e) {
                    bot.respond(nick, action.getResponseMode(), e.getMessage());
                }
            }
        }
        return PluginResult.NEXT;
    }
