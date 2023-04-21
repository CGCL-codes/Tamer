    void parse(IrcServerMessage message) {
        if (message.getCommand().equals("PING")) {
            ircConn.sendLine("PONG :" + message.getData());
        }
        if (message.getCommand().equals("MODE")) {
            if (!isRegistered()) {
                setRegistered(true);
                joinChannels(getChannels());
            }
        }
        if (!observers.isEmpty()) {
            for (int i = 0; i < observers.size(); i++) {
                observers.get(i).serverMessageReceived(message);
            }
        }
    }
