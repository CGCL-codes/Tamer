    protected void onChannelData(SshMsgChannelData msg) throws IOException {
        synchronized (incoming) {
            if (boundOutputStream != null) {
                try {
                    boundOutputStream.write(msg.getChannelData());
                } catch (IOException ex) {
                    log.info("Could not route data to the bound OutputStream; Closing channel.");
                    log.info(ex.getMessage());
                    close();
                }
            } else {
                incoming.addMessage(msg);
            }
        }
    }
