    public synchronized void start() throws IOException {
        debug.write("going to start SMSCListener on port " + port);
        if (!isReceiving) {
            serverConn = new org.smpp.TCPIPConnection(port);
            serverConn.setReceiveTimeout(getAcceptTimeout());
            serverConn.open();
            keepReceiving = true;
            if (asynchronous) {
                debug.write("starting listener in separate thread.");
                Thread serverThread = new Thread(this);
                serverThread.start();
                debug.write("listener started in separate thread.");
            } else {
                debug.write("going to listen in the context of current thread.");
                run();
            }
        } else {
            debug.write("already receiving, not starting the listener.");
        }
    }
