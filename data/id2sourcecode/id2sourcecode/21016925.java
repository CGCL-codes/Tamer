    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        String targetName = m.getParameter(0);
        ClientRepository repository = ClientRepository.getInstance();
        Client target = repository.getClient(targetName);
        if (target == null) {
            PlineMessage response = new PlineMessage();
            response.setKey("command.player_not_found", targetName);
            client.send(response);
        } else {
            Channel channel = target.getChannel();
            ChannelConfig channelConfig = channel.getConfig();
            if (target == client) {
                PlineMessage cantgoto = new PlineMessage();
                cantgoto.setKey("command.goto.yourself");
                client.send(cantgoto);
            } else if (channel == client.getChannel()) {
                PlineMessage cantgoto = new PlineMessage();
                cantgoto.setKey("command.goto.same_channel", target.getUser().getName());
                client.send(cantgoto);
            } else if (channel.isFull()) {
                PlineMessage channelfull = new PlineMessage();
                channelfull.setKey("command.join.full");
                client.send(channelfull);
            } else if (client.getUser().getAccessLevel() < channelConfig.getAccessLevel()) {
                PlineMessage accessDenied = new PlineMessage();
                accessDenied.setKey("command.join.denied");
                client.send(accessDenied);
            } else if (channelConfig.isPasswordProtected()) {
                PlineMessage accessDenied = new PlineMessage();
                accessDenied.setKey("command.goto.password");
                client.send(accessDenied);
            } else if (client.getUser().isPlayer() && !channelConfig.isProtocolAccepted(client.getProtocol().getName())) {
                String type = channelConfig.getSpeed() == Speed.FAST ? "TetriFast" : "TetriNET";
                client.send(new PlineMessage("command.join.speed", type));
            } else {
                AddPlayerMessage move = new AddPlayerMessage(client);
                channel.send(move);
            }
        }
    }
