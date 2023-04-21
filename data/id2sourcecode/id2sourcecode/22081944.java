    void handleReconnect() {
        int i;
        IRCPanel panel;
        IRCConnection connection;
        String channel;
        for (i = 0; i < panels.size(); i++) {
            panel = (IRCPanel) panels.elementAt(i);
            channel = panel.getChannelName();
            connection = client.getConnection(id);
            connection.joinChannel(channel);
        }
    }
