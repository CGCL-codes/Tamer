    public void run() {
        try {
            transmitMTU = connection.getTransmitMTU();
            Writer writer = new Writer(this);
            Thread writeThread = new Thread(writer);
            writeThread.start();
            listener.handleStreamsOpen(this);
        } catch (IOException e) {
            close();
            listener.handleStreamsOpenError(this, e.getMessage());
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
