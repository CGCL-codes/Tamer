    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        String targetName = m.getParameter(0);
        Client target = m.getClientParameter(0);
        if (target == null) {
            client.send(new PlineMessage("command.player_not_found", targetName));
        } else {
            Channel channel = m.getChannelParameter(1);
            if (channel != null) {
                if (channel.isFull()) {
                    PlineMessage channelfull = new PlineMessage();
                    channelfull.setKey("command.join.full");
                    client.send(channelfull);
                } else {
                    AddPlayerMessage move = new AddPlayerMessage(target);
                    channel.send(move);
                    PlineMessage teleported = new PlineMessage();
                    teleported.setKey("command.teleport.message", target.getUser().getName(), channel.getConfig().getName());
                    client.send(teleported);
                }
            }
        }
    }
