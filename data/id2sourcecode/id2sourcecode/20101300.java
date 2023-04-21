    public void proxyEvent(IObjectProxyEvent event) {
        double channelNumber = AppContext.instance().getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        clientTransceiver.addReceiver(channelNumber);
        ProxyServerAction action = new ProxyServerAction(event.getUniqueId(), -1);
        action.setTransceiver(clientTransceiver);
        ActionTools.sendToServer(action);
    }
