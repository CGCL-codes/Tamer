    public void sendFrame(CEMI _CEMI, boolean _Mode) throws EICLException {
        sendMode = _Mode;
        if (sendMode == IMMEDIATE_SEND && sendState < AWAITING_CEMI_CON) throw new EICLException("Last packet ack not received yet");
        if (sendMode == IMMEDIATE_SEND) sendState = WRONG_MODE;
        long time = 0;
        CEMI_Connection_Request request = new CEMI_Connection_Request(connection.getRequest(), server.getChannelid(), server.getSendSequence(), _CEMI);
        try {
            DatagramPacket packet = new DatagramPacket(request.toByteArray(), request.toByteArray().length, server.getDataEndpoint());
            socket.send(packet);
        } catch (Exception ex) {
            throw new EICLException(ex.getMessage());
        }
        if (sendMode == WAIT_FOR_CONFIRM) {
            synchronized (this) {
                try {
                    time = System.currentTimeMillis();
                    wait(SEND_TIMEOUT * 1000);
                } catch (InterruptedException ex) {
                }
            }
            if (System.currentTimeMillis() - time >= SEND_TIMEOUT * 1000) throw new EICLException("Send error: Could not send packet");
        } else {
            sendState = AWAITING_ACK;
        }
    }
