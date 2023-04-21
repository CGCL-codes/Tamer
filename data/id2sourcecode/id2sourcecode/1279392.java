    public final void sendMessage(byte messageFlag, String messageString) {
        try {
            sendMessage(new Message(messageFlag, messageString.getBytes(remoteClient.getStringEncoding())));
        } catch (UnsupportedEncodingException e) {
            logManager.writeToLog(1, "NET", "[" + threadID + "] Message Encoding Error: " + e.getMessage());
        }
    }
