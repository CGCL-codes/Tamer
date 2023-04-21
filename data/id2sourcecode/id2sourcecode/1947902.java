    public static void sendToServer(Action action) {
        if (action.getTransceiver() == null) {
            ClientTransceiver ct = new ClientTransceiver(AppContext.instance().getChannelNumber());
            ct.addReceiver(AppContext.instance().getChannelNumber());
            action.setTransceiver(ct);
        }
        Engine.instance().getActionProcessorRegistry().get("Client.SendEntryNetworkActionProcessor").perform(action);
    }
