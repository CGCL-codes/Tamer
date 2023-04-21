    public void perform() {
        if (source == CLIENT) {
            ((ClientTransceiver) getTransceiver()).getConnectedChannel().setAliveCheckSent(false);
            return;
        }
        double channelNumber = AppContext.instance().getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        clientTransceiver.addReceiver(channelNumber);
        AliveCheckServerAction aliveCheckServerAction = new AliveCheckServerAction(source);
        aliveCheckServerAction.setTransceiver(clientTransceiver);
        ActionTools.sendToServer(aliveCheckServerAction);
    }
