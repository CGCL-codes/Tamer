    private void closeDown() {
        try {
            try {
                if (socket != null && socket.isConnected() && socket.socket().isConnected() && socket.isOpen()) {
                    try {
                        socket.socket().shutdownInput();
                        socket.socket().shutdownOutput();
                    } catch (SocketException e) {
                    }
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    WritableByteChannel wc = out.getChannel();
                    if (wc != null) {
                        wc.close();
                    }
                    out.close();
                }
                if (socket != null) {
                    proxy.getCounter().inc("Client sockets closed");
                    socket.socket().close();
                    socket.close();
                }
            } catch (IOException e) {
                proxy.logError(Logger.WARN, "Problems to close the sockets are usually a bad thing..." + e);
                e.printStackTrace();
            }
            clearVariables();
        } finally {
            proxy.removeConnection(this);
        }
    }
