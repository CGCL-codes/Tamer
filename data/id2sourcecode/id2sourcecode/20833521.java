    public void onInit() throws XMPPException {
        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(hostname, port, serviceName);
        connection = new XMPPConnection(connectionConfiguration);
        connection.connect();
        connection.login(username, password);
        PacketListener myListener = new PacketListener() {

            public void processPacket(Packet packet) {
                try {
                    if (packet instanceof Message) {
                        Message message = (Message) packet;
                        if (!StringUtils.isEmpty(message.getBody())) {
                            Alert alert = messageDecoder.decodeMessage((Message) packet);
                            for (AlertListener alertListener : listeners) {
                                alertListener.onAlert(alert);
                            }
                        }
                    } else {
                        logger.error("Impossible has occured :( a message of type message is not a message:{}", packet);
                    }
                } catch (Throwable e) {
                    logger.error("Exception in listener block", e);
                }
            }
        };
        connection.addPacketListener(myListener, new PacketTypeFilter(Message.class));
    }
