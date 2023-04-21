    public void start() {
        boolean success = false;
        setStatus(STATUS_CONNECTING);
        if (incomingConnection) {
            success = accept();
        } else {
            success = connect();
        }
        if (!success) {
            close();
            setStatus(STATUS_DISCONNECTED);
        } else {
            setStatus(STATUS_CONNECTED);
            try {
                socket.setSoTimeout(Constants.DEFAULT_SOCKET_TIMEOUT);
            } catch (SocketException se) {
                Debug.log(se.toString());
            }
            writerThread.start();
            readerThread.start();
        }
    }
