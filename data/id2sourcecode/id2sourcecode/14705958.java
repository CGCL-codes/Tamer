    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        Channel channel = client.getChannel();
        if (channel != null) {
            StopGameMessage stop = new StopGameMessage();
            stop.setSlot(channel.getClientSlot(client));
            stop.setSource(client);
            channel.send(stop);
        }
    }
