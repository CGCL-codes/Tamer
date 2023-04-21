    private void handleFileAnswer(IEnvelope msg) {
        SendFileAnswer answer = (SendFileAnswer) msg.getMessage();
        FileTransfer info = getTransferInfo(answer.getFiletransferID(), State.REQUESTED, false);
        SendFileAnswer sfa = (SendFileAnswer) msg.getMessage();
        if (info == null) {
            Logger.logError(new IllegalStateException(msg.getSender() + " acknowlegded a file receival which we did not initiate!"), "Error in file transfer! Closing connection...");
            try {
                msg.getChannel().close();
            } catch (IOException e) {
                Logger.logError(e, "Error closing the connection to the peer which violated protocol: " + msg.getSender() + ". Ignoring...");
            }
            return;
        }
        info.setPeerNick(sfa.getNickname());
        if (sfa.canSend()) {
            info.setState(State.SENDING);
            internalSendFile((OutgoingFileTransfer) info, (SocketChannel) msg.getChannel());
        } else {
            info.setState(State.DENIED);
            try {
                msg.getChannel().close();
            } catch (IOException e) {
            }
        }
    }
