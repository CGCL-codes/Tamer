    private void connect() {
        if (nick == null || nick.length() == 0) {
            nick = "webcamstudio";
        }
        String server = location.split(":")[0];
        int port = new Integer(location.split(":")[1]).intValue();
        String channel = location.split(":")[2];
        state = new ClientState();
        state.addChannel(channel);
        connection = new IRCConnection(state);
        new AutoRegister(connection, nick, nick + "_" + new java.util.Random().nextInt(), nick + "_" + new java.util.Random().nextInt(), password);
        autoReconnect = new AutoReconnect(connection);
        new AutoJoin(connection, channel, null);
        messageMonitor = new MessageMonitor(this);
        autoReconnect.go(server, port);
        mainChannel = state.getChannel(channel);
        if (connection.getState() == State.UNCONNECTED) {
            error("IRC: " + name + " (Could not connect)");
            disconnect();
        } else {
            info("Connected");
        }
    }
