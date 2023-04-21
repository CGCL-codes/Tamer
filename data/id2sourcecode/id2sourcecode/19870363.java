    public boolean sendIPCMessage(byte[] message, int offset, int len, int to_thread_id, AceThread sender, InetAddress addr, int port) {
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
        AceIPCServerConnection client_conn = findClient(addr, port);
        if (client_conn == null) {
            writeErrorMessage("The client is not currently connected");
            retval = false;
        } else if (client_conn.resetSendTiming() == false) {
            dropConnection(client_conn, true);
            writeErrorMessage("Fatal timing error encountered, connection dropped");
            retval = false;
        } else {
            AceIPCUserMessage ipc_msg = new AceIPCUserMessage(to_thread_id, ((AceThread) parent_thread).getAceThreadId(), message, offset, len);
            if (sendMessage(ipc_msg, addr, port) == false) {
                writeErrorMessage("Socket error sending message, connection dropped");
                retval = false;
            }
        }
        return retval;
    }
