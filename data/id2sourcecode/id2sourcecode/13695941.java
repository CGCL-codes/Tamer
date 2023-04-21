    public void iObjectListEvent(IObjectListEvent event) {
        IObject newProt = event.getObject();
        double channelNumber = AppContext.instance().getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        if (event.getType() == IObjectListEvent.REMOVE) {
            ProxyLinkedListRemoveServerAction action = new ProxyLinkedListRemoveServerAction(event.getOwnerObject().getUniqueId(), event.getListAttribute(), newProt);
            clientTransceiver.addReceiver(clientTransceiver.getSender());
            action.setTransceiver(clientTransceiver);
            ActionTools.sendToServer(action);
            return;
        }
        if (newProt.getUniqueId() > 0) {
            return;
        }
        long newUniqueId = newProt.getUniqueId();
        ((User) AppContext.instance().getUser()).putNewObjectsMapping(new Long(newUniqueId), new Long(newUniqueId));
        clientTransceiver.addReceiver(channelNumber);
        ProxyLinkedListAddServerAction action = new ProxyLinkedListAddServerAction(event.getOwnerObject().getUniqueId(), event.getListAttribute(), newProt);
        action.setTransceiver(clientTransceiver);
        ActionProcessorRegistry actionProcessorRegistry = Engine.instance().getActionProcessorRegistry();
        ActionTools.sendToServer(action);
    }
