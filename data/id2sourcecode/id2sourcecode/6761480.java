    public void handle(Event ev) {
        if (ev instanceof BroadcastSendableEvent) handleBroadcastSendableEvent((BroadcastSendableEvent) ev); else if (ev instanceof GetPeersEvent) handleGetPeersEvent((GetPeersEvent) ev); else if (ev instanceof CleanUpTimer) handleGetPeersEvent((CleanUpTimer) ev); else if (ev instanceof P2PInitEvent) handleP2PInitEvent((P2PInitEvent) ev); else if (ev instanceof ChannelInit) {
            this.channel = ((ChannelInit) ev).getChannel();
            try {
                ev.go();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ev.go();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
        }
    }
