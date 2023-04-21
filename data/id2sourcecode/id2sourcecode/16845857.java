    private void handleFileRequest(IEnvelope request) {
        SendFileRequest sfr = (SendFileRequest) request.getMessage();
        File saveTo = null;
        IPeer from = request.getSender();
        do {
            saveTo = chatModel.onSendFileRequest(from, sfr.getNickname(), sfr.getFilename(), sfr.getSize());
        } while (saveTo != null && !isValidSaveTarget(saveTo));
        SendFileAnswer sfa = new SendFileAnswer(myself.getPeerID(), sfr.getSenderPeerID(), sfr.getFiletransferID(), saveTo != null, chatModel.getMyself().getNickname());
        try {
            if (saveTo != null) {
                FileTransfer ft = addIncomingFiletransfer(sfr.getFiletransferID(), from, sfr.getNickname(), sfr.getFilename(), sfr.getSize(), saveTo);
                chatModel.onStartedFileTransfer(ft);
                ft.setState(State.SENDING);
            }
            commFacade.sendTCPMessage((SocketChannel) request.getChannel(), sfa);
        } catch (IOException e) {
            Logger.logError(e, "Error sending answer to file offer!");
        }
    }
