    public void disconnect(String _Message) throws EICLException {
        long time = 0;
        Disconnect_Request request = new Disconnect_Request(server.getChannelid(), socket.getLocalPort());
        try {
            DatagramPacket packet = new DatagramPacket(request.toByteArray(), request.toByteArray().length, server.getControlEndpoint());
            socket.send(packet);
        } catch (Exception ex) {
            throw new EICLException(ex.getMessage());
        }
        synchronized (this) {
            try {
                time = System.currentTimeMillis();
                wait(DISCONNECT_TIMEOUT * 1000);
            } catch (InterruptedException ex) {
            }
        }
        if (System.currentTimeMillis() - time > DISCONNECT_TIMEOUT * 1000) {
            stopThread();
            socket.close();
            _Message = ("Disconnect error: Request timed out");
        }
        cleanUp(_Message);
    }
