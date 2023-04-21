    private void onJoin(String host, String channel) {
        Channel chanRecord = getChannel(channel, true);
        if (libairc.getNickFromHost(host).equalsIgnoreCase(nick)) {
            libairc.debug("BotJoin", "Channel: " + channel);
            new Thread(new channelJoinInvoker(incoming.catchMessages(), channel)).start();
        } else {
            libairc.debug("Join", "Nick: " + libairc.getNickFromHost(host) + ", Channel: " + channel);
            User user = getUser(libairc.getNickFromHost(host));
            if (user == null) {
                libairc.debug("Join", "Unknown user: " + libairc.getNickFromHost(host));
                return;
            }
            chanRecord.addUser(user);
        }
    }
