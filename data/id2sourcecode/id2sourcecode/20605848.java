    protected void onRecv(byte[] b, int off, int len) {
        Vector setSocket = getServerThread().getSocketSet();
        synchronized (setSocket) {
            for (int i = 0; i < setSocket.size(); i++) {
                try {
                    ((SocketThread) setSocket.elementAt(i)).getOutputStream().write(b, off, len);
                } catch (IOException e) {
                }
            }
        }
    }
