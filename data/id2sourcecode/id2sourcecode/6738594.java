    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        Channel channel = client.getChannel();
        if (channel != null) {
            ChannelMessage pause = null;
            if (channel.getGameState() == PAUSED) {
                pause = new ResumeMessage();
            } else {
                pause = new PauseMessage();
            }
            pause.setSlot(channel.getClientSlot(client));
            pause.setSource(client);
            channel.send(pause);
        }
    }
