    public void perform() {
        double channelNumber = AppContext.instance().getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        clientTransceiver.addReceiver(channelNumber);
        IObjectProxy iObjectProxy = Engine.instance().getProxyRegistry().getProxy(prototype.getUniqueId());
        iObjectProxy.setUpToDate(false);
        EditIObjectServerAction editPrototypeServerAction = new EditIObjectServerAction(prototype);
        editPrototypeServerAction.setTransceiver(clientTransceiver);
        ActionTools.sendToServer(editPrototypeServerAction);
    }
