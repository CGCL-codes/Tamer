    public void update(Observable observer, Object updated) {
        if (updated instanceof PingCommand) {
            PingCommand ping = (PingCommand) updated;
            String response = ping.getPingSource();
            connection.sendCommand(new PongCommand(response));
        } else if (updated instanceof JoinCommand) {
            JoinCommand join = (JoinCommand) updated;
            if (join.weJoined(connection.getClientState())) {
                connection.sendCommand(new ChannelModeCommand(join.getChannel()));
            }
        }
    }
