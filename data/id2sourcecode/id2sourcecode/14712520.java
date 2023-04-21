    public void open() throws IOException {
        debug.enter(DCOM, this, "open");
        IOException exception = null;
        if (!opened) {
            if (connType == CONN_CLIENT) {
                try {
                    socket = socketFactory.createSocket(address, port);
                    initialiseIOStreams(socket);
                    opened = true;
                    debug.write(DCOM, "opened client tcp/ip connection to " + address + " on port " + port);
                } catch (IOException e) {
                    debug.write("IOException opening TCPIPConnection " + e);
                    event.write(e, "IOException opening TCPIPConnection");
                    exception = e;
                }
            } else if (connType == CONN_SERVER) {
                try {
                    receiverSocket = serverSocketFactory.createServerSocket(port);
                    opened = true;
                    debug.write(DCOM, "listening tcp/ip on port " + port);
                } catch (IOException e) {
                    debug.write("IOException creating listener socket " + e);
                    exception = e;
                }
            } else {
                debug.write("Unknown connection type = " + connType);
            }
        } else {
            debug.write("attempted to open already opened connection ");
        }
        debug.exit(DCOM, this);
        if (exception != null) {
            throw exception;
        }
    }
