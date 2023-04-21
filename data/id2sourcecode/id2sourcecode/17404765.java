    void onJoinHelper(Catcher catcher, String channel) {
        Channel chanRec = getChannel(channel, true);
        String[] message;
        String nick;
        boolean catching = true;
        boolean names = false;
        boolean topic = false;
        boolean mode = false;
        while (catching) {
            catcher.waitForMessages();
            message = catcher.getNextMessage().split(" ");
            if (message.length > 5 && message[1].equalsIgnoreCase("353") && message[4].equalsIgnoreCase(channel)) {
                message[5] = message[5].substring(1);
                for (int index = 5; index < message.length; index++) {
                    nick = libairc.getNickFromNames(message[index]);
                    if (!this.nick.equalsIgnoreCase(nick)) {
                        if (!users.containsKey(nick)) {
                            libairc.debug("Names", "Added user record for: " + nick);
                            users.put(nick, new User(this, nick));
                        }
                        chanRec.addUser(getUser(nick));
                    }
                }
            } else if (message.length > 4 && message[1].equalsIgnoreCase("366") && message[3].equalsIgnoreCase(channel)) {
                incoming.stopCatchingMessages(catcher);
                catching = false;
            }
        }
    }
