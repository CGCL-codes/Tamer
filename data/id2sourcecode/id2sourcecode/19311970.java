    public void run() {
        String url = null;
        try {
            url = serviceRecord.getConnectionURL(requiredSecurity, false);
            connection = (L2CAPConnection) Connector.open(url);
            transmitMTU = connection.getTransmitMTU();
            LogScreen.log("Opened connection to: '" + url + "'\n");
            Writer writer = new Writer(this);
            Thread writeThread = new Thread(writer);
            writeThread.start();
            LogScreen.log("Started a reader & writer for: '" + url + "'\n");
            listener.handleOpen(this);
        } catch (IOException e) {
            LogScreen.log("Failed to open " + "connection for '" + url + "' , Error: " + e.getMessage());
            close();
            listener.handleOpenError(this, "IOException :'" + e.getMessage() + "'");
            return;
        } catch (SecurityException e) {
            LogScreen.log("Failed to open " + "connection for '" + url + "' , Error: " + e.getMessage());
            close();
            listener.handleOpenError(this, "SecurityException: '" + e.getMessage() + "'");
            return;
        }
        while (!aborting) {
            boolean ready = false;
            try {
                ready = connection.ready();
            } catch (IOException e) {
                close();
                listener.handleClose(this);
            }
            int length = 0;
            try {
                if (ready) {
                    int mtuLength = connection.getReceiveMTU();
                    if (mtuLength > 0) {
                        byte[] buffer = new byte[mtuLength];
                        length = connection.receive(buffer);
                        byte[] readData = new byte[length];
                        System.arraycopy(buffer, 0, readData, 0, length);
                        listener.handleReceivedMessage(this, readData);
                    }
                } else {
                    try {
                        synchronized (this) {
                            wait(WAIT_MILLIS);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            } catch (IOException e) {
                close();
                if (length == 0) {
                    listener.handleClose(this);
                } else {
                    listener.handleErrorClose(this, e.getMessage());
                }
            }
        }
    }
