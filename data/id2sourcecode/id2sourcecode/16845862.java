    private void handleFileCompleteMessage(IEnvelope env) {
        FileCompleteMessage msg = (FileCompleteMessage) env.getMessage();
        OutgoingFileTransfer ft = (OutgoingFileTransfer) this.getTransferInfo(msg.getFiletransferID(), State.SENDING, false);
        ft.setTransferred(ft.getSize());
        ft.receiveCompleteMessage();
        try {
            env.getChannel().close();
        } catch (IOException e) {
        }
    }
