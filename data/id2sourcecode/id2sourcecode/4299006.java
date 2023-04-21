    public void messageReceived(final IoSession iosession, final Object message) throws Exception {
        if (message instanceof Protocol) {
            Protocol header = (Protocol) message;
            try {
                switch(header.getProtocol()) {
                    case Protocol.PROTOCOL_SESSION_MESSAGE:
                        {
                            Listener.receivedMessage(this, header, header.getMessage());
                            break;
                        }
                    case Protocol.PROTOCOL_CHANNEL_MESSAGE:
                        {
                            ClientChannelImpl channel = null;
                            synchronized (channels) {
                                channel = channels.get(header.getChannelID());
                                if (channel == null) {
                                    channel = new ClientChannelImpl(this, header.getChannelID());
                                    channels.putIfAbsent(header.getChannelID(), channel);
                                }
                            }
                            Listener.receivedChannelMessage(channel, header, header.getMessage());
                            break;
                        }
                    case Protocol.PROTOCOL_CHANNEL_JOIN_S2C:
                        {
                            synchronized (channels) {
                                channels.put(header.getChannelID(), new ClientChannelImpl(this, header.getChannelID()));
                            }
                            break;
                        }
                    case Protocol.PROTOCOL_CHANNEL_LEAVE_S2C:
                        {
                            synchronized (channels) {
                                ClientChannelImpl channel = channels.get(header.getChannelID());
                                if (channel != null) {
                                    Listener.leftChannel(channel);
                                    channels.remove(header.getChannelID());
                                }
                            }
                            break;
                        }
                    case ProtocolImpl.PROTOCOL_SYSTEM_NOTIFY:
                        {
                            Listener.receivedMessage(this, header, header.getMessage());
                            break;
                        }
                    default:
                        log.error("messageReceived : " + "unknow protocol(" + Integer.toString(header.getProtocol(), 16) + ")" + " : " + "data : " + header);
                }
            } catch (Exception e) {
                log.error("messageReceived : message process error : " + header);
                e.printStackTrace();
            }
        } else {
            log.error("messageReceived : bad message type : " + message);
        }
    }
