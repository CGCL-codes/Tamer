    private void handleFileDataMessage(IEnvelope env) {
        FileDataMessage fdm = (FileDataMessage) env.getMessage();
        IPeer from = env.getSender();
        IncomingFileTransfer ft = (IncomingFileTransfer) getTransferInfo(fdm.getFiletransferID(), State.SENDING, true);
        if (ft == null) {
            Logger.logError(new IllegalStateException("Illegal message: Peer " + env.getSender() + " just tries to send us a file! Closing connection..."), "Message not valid due to protocol!");
            try {
                env.getChannel().close();
            } catch (IOException e) {
            }
        }
        boolean sendCompleteMessage = ft.handleFileDataMessage(fdm);
        if (sendCompleteMessage) {
            try {
                IMessage msg = new FileCompleteMessage(myself.getPeerID(), from.getPeerID(), fdm.getFiletransferID());
                commFacade.sendTCPMessage((SocketChannel) env.getChannel(), msg);
                ft.setState(State.FINISHED);
            } catch (IOException e) {
            }
        }
    }
