    public void processMessage(IRCServer sendServer, String channel, String sender, String login, String hostname, String message) {
        ServerWithWnd useServer = null;
        for (ServerWithWnd server : serverList) {
            if (server.server == sendServer) {
                useServer = server;
                break;
            }
        }
        if (useServer != null) {
            for (final ChannelWindow cWndo : useServer.wnd.getChannelWindows().values()) {
                if (cWndo.getChannel().equalsIgnoreCase(channel)) {
                    cWndo.dropMessage(sender, message);
                }
            }
        }
    }
