    private synchronized void shutdownConnection() {
        if (conn == null && in == null && out == null) return;
        updateState(DISCONNECTED);
        close(conn, in, out);
        in = null;
        out = null;
        conn = null;
        if (writeThread != null) {
            Vector inbox = writeThread.getInbox();
            for (int c = 0; c < inbox.size(); c++) {
                addToOfflineBox(inbox.elementAt(c), false);
            }
            writeThread.clearInbox();
            disconnected();
        }
    }
