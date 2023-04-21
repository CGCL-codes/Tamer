    private void handleChannelInit(ChannelInit e) {
        if (gossips == null || gossips.length == 0) {
            logger.fatal("Received channel init but no gossip is configured.");
            throw new AppiaError("Received channel init but no gossip is configured.");
        }
        try {
            e.go();
        } catch (AppiaEventException e1) {
            e1.printStackTrace();
        }
        channels.add(e.getChannel());
        numberOfChannels = channels.size();
        if (!sentRSE) {
            RegisterSocketEvent rse = null;
            try {
                rse = new RegisterSocketEvent(e.getChannel(), Direction.DOWN, this, RegisterSocketEvent.FIRST_AVAILABLE);
                if (myAddress != null) {
                    rse.localHost = myAddress.getAddress();
                    rse.port = myAddress.getPort();
                }
                rse.go();
                sentRSE = true;
            } catch (AppiaEventException ex) {
                switch(ex.type) {
                    case AppiaEventException.UNWANTEDEVENT:
                        System.err.println("The QoS definition doesn't satisfy the " + "application session needs. " + "RegisterSocketEvent, received by " + "UdpSimpleSession is not being accepted");
                        break;
                    default:
                        System.err.println("Unexpected exception in " + this.getClass().getName());
                        break;
                }
            }
            if (multicast != null) {
                try {
                    new MulticastInitEvent(multicast, false, e.getChannel(), Direction.DOWN, this).go();
                } catch (AppiaEventException ex) {
                    throw new AppiaError("Impossible to send Multicast Init Event.", ex);
                }
            }
        }
        openChannel.countDown();
    }
