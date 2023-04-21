    public void perform() {
        double channelNumber = appContext.getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        clientTransceiver.addReceiver(channelNumber);
        if (message.startsWith("/")) {
            ConsoleManager consoleManager = (ConsoleManager) Engine.instance().getManagerRegistry().getManager("console");
            try {
                consoleManager.doConsoleCommand(message.substring(1, message.length()));
            } catch (Exception x) {
            }
        } else {
            ChatMessageServerAction chatMessageServerAction = new ChatMessageServerAction(message, channelId, userName);
            chatMessageServerAction.setTransceiver(clientTransceiver);
            ActionTools.sendToServer(chatMessageServerAction);
        }
    }
