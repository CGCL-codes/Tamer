    private void sendOutstandingMessages() throws IOException {
        if ((boundInputStream != null) && (boundOutputStream != null) && incoming.hasMessages()) {
            while (true) {
                try {
                    SshMsgChannelData msg = (SshMsgChannelData) incoming.peekMessage(SshMsgChannelData.SSH_MSG_CHANNEL_DATA);
                    incoming.removeMessage(msg);
                    try {
                        boundOutputStream.write(msg.getChannelData());
                    } catch (IOException ex1) {
                        close();
                    }
                } catch (MessageStoreEOFException ex) {
                    break;
                } catch (MessageNotAvailableException ex) {
                    break;
                } catch (InterruptedException ex) {
                    throw new IOException("The thread was interrupted");
                }
            }
        }
    }
