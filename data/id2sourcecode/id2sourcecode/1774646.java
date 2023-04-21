    public void packetReceived(Connection conn, SMPPPacket pack) {
        switch(pack.getCommandId()) {
            case SMPPPacket.DELIVER_SM:
                try {
                    InOnly exchange = getExchangeFactory().createInOnlyExchange();
                    NormalizedMessage in = exchange.createMessage();
                    exchange.setInMessage(in);
                    marshaler.toNMS(in, pack);
                    getChannel().send(exchange);
                } catch (Exception e) {
                    LOG.error("Unable to send the received packet to nmr.", e);
                }
                break;
            case SMPPPacket.BIND_TRANSCEIVER_RESP:
                if (pack.getCommandStatus() != 0) {
                    LOG.error("Error binding: " + pack.getCommandStatus());
                    reconnect();
                } else {
                    LOG.debug("Bounded");
                }
                break;
            default:
                LOG.debug("Received packet with unhandled command id: " + pack.getCommandId());
        }
    }
