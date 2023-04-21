    public void run() {
        String url = null;
        try {
            url = serviceRecord.getConnectionURL(requiredSecurity, false);
            connection = (StreamConnection) Connector.open(url);
            in = connection.openInputStream();
            out = connection.openOutputStream();
            LogScreen.log("Opened connection & streams to: '" + url + "'\n");
            Writer writer = new Writer(this);
            Thread writeThread = new Thread(writer);
            writeThread.start();
            LogScreen.log("Started a reader & writer for: '" + url + "'\n");
            listener.handleOpen(this);
        } catch (IOException e) {
            LogScreen.log("Failed to open " + "connection or streams for '" + url + "' , Error: " + e.getMessage());
            close();
            listener.handleOpenError(this, "IOException: '" + e.getMessage() + "'");
            return;
        } catch (SecurityException e) {
            LogScreen.log("Failed to open " + "connection or streams for '" + url + "' , Error: " + e.getMessage());
            close();
            listener.handleOpenError(this, "SecurityException: '" + e.getMessage() + "'");
            return;
        }
        while (!aborting) {
            int length = 0;
            try {
                byte[] lengthBuf = new byte[LENGTH_MAX_DIGITS];
                readFully(in, lengthBuf);
                length = readLength(lengthBuf);
                byte[] temp = new byte[length];
                readFully(in, temp);
                listener.handleReceivedMessage(this, temp);
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
