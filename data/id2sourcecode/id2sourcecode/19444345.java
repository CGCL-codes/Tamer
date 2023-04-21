    public void perform() {
        double channelNumber = appContext.getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        clientTransceiver.addReceiver(channelNumber);
        UserLoginServerAction userLoginServerAction = new UserLoginServerAction(username, password);
        userLoginServerAction.setTransceiver(clientTransceiver);
        ActionTools.sendToServer(userLoginServerAction);
    }
