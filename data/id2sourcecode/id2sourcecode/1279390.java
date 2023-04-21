    private final void close() {
        try {
            stateManager.removeRemoteClient(threadID);
            in.close();
            dout.close();
            out.close();
            socket.close();
            synchronized (this) {
                activeThreadCounter--;
            }
            logManager.writeToLog(4, "NET", "[" + threadID + "] Socket closed. (" + remoteClient.getIPAddressString() + ")");
        } catch (IOException e) {
            logManager.writeToLog(1, "NET", "[" + threadID + "] Socket close error: " + e.getMessage());
        }
    }
