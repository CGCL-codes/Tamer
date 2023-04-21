    public void perform() {
        double channelNumber = appContext.getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        clientTransceiver.addReceiver(channelNumber);
        UserJoinServerAction userJoinServerAction = new UserJoinServerAction(userName, channel);
        userJoinServerAction.setTransceiver(clientTransceiver);
        ActionTools.sendToServer(userJoinServerAction);
    }
