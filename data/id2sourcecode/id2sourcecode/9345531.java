    private void onReceivedPingPong(IEnvelope envelope) {
        if (envelope.getMessage() instanceof UDPPong) {
            Logger.log("RECEIVED a pong from " + envelope.getSender() + "!");
            return;
        }
        DatagramChannel dc = (DatagramChannel) envelope.getChannel();
        InetSocketAddress sender = envelope.getSender().getAddress();
        try {
            getCommFacade().sendUDPMessage(dc, new UDPPong(myself.getPeerID(), envelope.getSender().getPeerID()), sender);
        } catch (IOException e) {
            Logger.logError(e, "Error answering to UDPPing of " + envelope.getSender());
        }
        return;
    }
