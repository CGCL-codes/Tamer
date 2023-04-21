    public boolean sendIPCMessage(byte[] message, int offset, int len, int to_thread_id, AceThread sender) {
        Thread parent_thread = null;
        if (sender == null) {
            parent_thread = Thread.currentThread();
        } else {
            parent_thread = sender;
        }
        if ((parent_thread instanceof AceThread) == false) {
            writeErrorMessage("The calling thread must be an instance of AceThread");
            return false;
        }
        boolean retval = true;
        synchronized (ipcLock) {
            if (state != STATE_CONNECTED) {
                writeErrorMessage("The client is not currently connected");
                retval = false;
            } else if (resetSendTimer(true) == false) {
                dropConnection();
                dropSocket();
                writeErrorMessage("Fatal timing error encountered");
                retval = false;
            } else {
                AceIPCUserMessage ipc_msg = new AceIPCUserMessage(to_thread_id, ((AceThread) parent_thread).getAceThreadId(), message, offset, len);
                if (sendMessage(ipc_msg) == false) {
                    reconnectWithNewSocket();
                    writeErrorMessage("Socket error sending message, attempting reconnect");
                    retval = false;
                }
            }
        }
        return retval;
    }
