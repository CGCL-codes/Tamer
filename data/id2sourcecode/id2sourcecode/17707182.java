    public void perform() {
        appContext = AppContext.instance();
        this.userName = appContext.getUser().getName();
        double channelNumber = appContext.getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        clientTransceiver.addReceiver(channelNumber);
        ChatClientManager chatManager = (ChatClientManager) Engine.instance().getManagerRegistry().getManager("chat.client");
        if (channel == -1) {
            channel = chatManager.getCurrentChannel().intValue();
        }
        UserLeaveServerAction userLeaveServerAction = new UserLeaveServerAction(userName, channel);
        userLeaveServerAction.setTransceiver(clientTransceiver);
        ActionTools.sendToServer(userLeaveServerAction);
    }
