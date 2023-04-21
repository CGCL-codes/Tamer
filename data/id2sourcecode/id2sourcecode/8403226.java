    public void messageReceived(final IoSession session, final Object message) throws Exception {
        if (message instanceof Protocol) {
            Protocol header = (Protocol) message;
            recordMessageCount(message_received_count, header);
            ClientSessionImpl client = getBindSession(session);
            if (client != null && client.Listener != null) {
                switch(header.getProtocol()) {
                    case Protocol.PROTOCOL_CHANNEL_MESSAGE:
                        ChannelImpl channel = (ChannelImpl) channel_manager.getChannel(header.getChannelID());
                        if (channel != null) {
                            channel.getChannelListener().receivedMessage(channel, client, header.getMessage());
                        }
                        break;
                    case Protocol.PROTOCOL_SESSION_MESSAGE:
                        client.Listener.receivedMessage(client, header, header.getMessage());
                        break;
                    default:
                        log.error("unknow message : " + session + " : " + message);
                }
            }
        } else {
            log.error("bad message type : " + session + " : " + message);
        }
    }
