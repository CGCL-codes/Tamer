    public ByteBuffer receive() throws IOException {
        debug.enter(DCOMD, this, "receive");
        IOException exception = null;
        ByteBuffer data = null;
        if (connType == CONN_CLIENT) {
            data = new ByteBuffer();
            long endTime = Data.getCurrentTime() + getReceiveTimeout();
            int bytesToRead = 0;
            int bytesRead = 0;
            int totalBytesRead = 0;
            try {
                socket.setSoTimeout((int) getCommsTimeout());
                bytesToRead = receiveBufferSize;
                debug.write(DCOMD, "going to read from socket");
                debug.write(DCOMD, "comms timeout=" + getCommsTimeout() + " receive timeout=" + getReceiveTimeout() + " receive buffer size=" + receiveBufferSize);
                do {
                    bytesRead = 0;
                    try {
                        bytesRead = inputStream.read(receiveBuffer, 0, bytesToRead);
                    } catch (InterruptedIOException e) {
                        debug.write(DCOMD, "timeout reading from socket");
                    }
                    if (bytesRead > 0) {
                        debug.write(DCOMD, "read " + bytesRead + " bytes from socket");
                        data.appendBytes(receiveBuffer, bytesRead);
                        totalBytesRead += bytesRead;
                    }
                    bytesToRead = inputStream.available();
                    if (bytesToRead > 0) {
                        debug.write(DCOMD, "more data (" + bytesToRead + " bytes) remains in the socket");
                    } else {
                        debug.write(DCOMD, "no more data remains in the socket");
                    }
                    if (bytesToRead > receiveBufferSize) {
                        bytesToRead = receiveBufferSize;
                    }
                    if (totalBytesRead + bytesToRead > maxReceiveSize) {
                        bytesToRead = maxReceiveSize - totalBytesRead;
                    }
                } while (((bytesToRead != 0) && (Data.getCurrentTime() <= endTime)) && (totalBytesRead < maxReceiveSize));
                debug.write(DCOM, "totally read " + data.length() + " bytes from socket");
            } catch (IOException e) {
                debug.write("IOException " + e);
                event.write(e, "IOException receive via TCPIPConnection");
                exception = e;
            }
        } else if (connType == CONN_SERVER) {
            debug.write("Attempt to receive data from server type connection.");
        } else {
            debug.write("Unknown connection type = " + connType);
        }
        debug.exit(DCOMD, this);
        if (exception != null) {
            throw exception;
        }
        return data;
    }
