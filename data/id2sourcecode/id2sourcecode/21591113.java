    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        Channel channel = client.getChannel();
        if (channel != null && channel.getGameState() == STOPPED) {
            int delay = 0;
            if (m.getParameterCount() > 0) {
                delay = m.getIntParameter(0, delay);
            }
            delay = Math.min(delay, 20);
            if (delay > 0) {
                PlineMessage message = new PlineMessage();
                message.setKey("channel.game.started-by", client.getUser().getName());
                channel.send(message);
                (new StartCommand.CountDown(channel, delay)).start();
            } else {
                StartGameMessage start = new StartGameMessage();
                start.setSlot(channel.getClientSlot(client));
                start.setSource(client);
                channel.send(start);
            }
        }
    }
