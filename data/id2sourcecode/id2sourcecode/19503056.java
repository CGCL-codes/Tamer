    private void collectNextMessage() throws MessageStoreEOFException, InterruptedException, IOException {
        startBlockingOperation();
        try {
            if (type != null) {
                SshMsgChannelExtendedData msg = null;
                while ((msg == null) && !isClosed()) {
                    try {
                        log.debug("Waiting for extended channel data");
                        msg = (SshMsgChannelExtendedData) messageStore.getMessage(filter, interrupt);
                    } catch (MessageNotAvailableException ex) {
                    }
                }
                if (msg != null) {
                    msgdata = msg.getChannelData();
                    currentPos = 0;
                } else {
                    throw new MessageStoreEOFException();
                }
            } else {
                SshMsgChannelData msg = null;
                while ((msg == null) && !isClosed()) {
                    try {
                        log.debug("Waiting for channel data");
                        msg = (SshMsgChannelData) messageStore.getMessage(filter, interrupt);
                    } catch (MessageNotAvailableException ex1) {
                    }
                }
                if (msg != null) {
                    msgdata = msg.getChannelData();
                    currentPos = 0;
                } else {
                    throw new MessageStoreEOFException();
                }
            }
        } finally {
            stopBlockingOperation();
        }
    }
