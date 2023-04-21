    ListNick[] getNickList() {
        IRCConnection connection;
        connection = application.getConnection(connectionID);
        if (connection == null || channel == null) return null;
        return connection.getChannel(channel).getNickList();
    }
