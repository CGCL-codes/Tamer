    public void run() {
        byte[] buffer = new byte[128];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        while (!stopThread) {
            try {
                socket.receive(datagramPacket);
                EIBnetIPPacket packet = new EIBnetIPPacket(datagramPacket.getData(), datagramPacket.getLength());
                if (packet.getServiceType() == EIBNETIP_Constants.CONNECT_RESPONSE) {
                    Connect_Response response = new Connect_Response(packet.getBody());
                    if (response.getStatus() != EIBNETIP_Constants.E_NO_ERROR) {
                        error = ("Connect error: " + Util.getStatusString(response.getStatus()));
                    } else {
                        server.setControlEndpoint(response.getDataEndPoint());
                        server.setDataEndpoint(response.getDataEndPoint());
                        server.setChannelid((byte) response.getChannelID());
                    }
                    synchronized (this) {
                        this.notify();
                    }
                } else if (packet.getServiceType() == EIBNETIP_Constants.CONNECTIONSTATE_RESPONSE) {
                    Connectionstate_Response response = new Connectionstate_Response(packet.getBody());
                    if (response.getStatus() != EIBNETIP_Constants.E_NO_ERROR) throw new EICLException("Connectionstate error: " + Util.getStatusString(response.getStatus()));
                    synchronized (heartbeat) {
                        heartbeat.notify();
                    }
                } else if (packet.getServiceType() == EIBNETIP_Constants.DISCONNECT_RESPONSE) {
                    Disconnect_Response response = new Disconnect_Response(packet.getBody());
                    if (response.getStatus() != EIBNETIP_Constants.E_NO_ERROR) throw new EICLException("Disconnect error: " + Util.getStatusString(response.getStatus()));
                    synchronized (this) {
                        stopThread();
                        this.notify();
                    }
                } else if (packet.getServiceType() == connection.getAck()) {
                    CEMI_Connection_Ack request = new CEMI_Connection_Ack(packet.getBody());
                    if (request.getChannelid() == server.getChannelid() && request.getSequencecounter() == server.getSendSequence()) {
                        server.incSendSequence();
                        sendState = AWAITING_CEMI_CON;
                    } else {
                    }
                } else if (packet.getServiceType() == connection.getRequest()) {
                    CEMI_Connection_Request request = new CEMI_Connection_Request(connection.getRequest(), packet.getBody());
                    if (request.getChannelid() == server.getChannelid()) {
                        if (request.getSequenceNumber() == server.getReceiveSequence() || request.getSequenceNumber() == server.getReceiveSequence() - 1) {
                            CEMI_Connection_Ack ack = new CEMI_Connection_Ack(connection.getAck(), request.getChannelid(), request.getSequenceNumber(), EIBNETIP_Constants.E_NO_ERROR);
                            DatagramPacket data = new DatagramPacket(ack.toByteArray(), ack.toByteArray().length, server.getControlEndpoint());
                            socket.send(data);
                            if (request.getSequenceNumber() == server.getReceiveSequence()) {
                                server.incReceiveSequence();
                                if (request.getCemi().getMessageCode() != CEMI_L_DATA.MC_L_DATACON) {
                                    FrameEvent event = new FrameEvent(this, request.getCemi());
                                    dispatcher.queue(event);
                                } else if (request.getCemi().getMessageCode() == CEMI_L_DATA.MC_L_DATACON) {
                                    if (((CEMI_L_DATA) request.getCemi()).isPositiveConfirmation()) {
                                        if (sendMode == IMMEDIATE_SEND) {
                                            sendState = OK;
                                        } else {
                                            synchronized (this) {
                                                this.notify();
                                            }
                                        }
                                        FrameEvent event = new FrameEvent(this, request.getCemi());
                                        dispatcher.queue(event);
                                    }
                                } else {
                                }
                            }
                        }
                    }
                } else if (packet.getServiceType() == EIBNETIP_Constants.DISCONNECT_REQUEST) {
                    Disconnect_Request request = new Disconnect_Request(packet.getBody());
                    Disconnect_Response response;
                    if (request.getChannelID() == server.getChannelid() && server.getControlEndpoint().equals(datagramPacket.getSocketAddress())) {
                        response = new Disconnect_Response(server.getChannelid(), EIBNETIP_Constants.E_NO_ERROR);
                        cleanUp("Disconnect requested by server");
                    } else {
                        response = new Disconnect_Response(request.getChannelID(), EIBNETIP_Constants.E_CONNECTION_ID);
                    }
                    DatagramPacket datapacket = new DatagramPacket(response.toByteArray(), response.toByteArray().length, datagramPacket.getSocketAddress());
                    socket.send(datapacket);
                }
            } catch (Exception ex) {
                stopThread = true;
            }
        }
    }
