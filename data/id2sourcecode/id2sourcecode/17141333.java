            @Override
            public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
                InetSocketAddress remote = (InetSocketAddress) e.getChannel().getRemoteAddress();
                if (!channelsByAddress.containsKey(remote)) {
                    return;
                }
                if (!(e.getMessage() instanceof AbstractMessage)) {
                    return;
                }
                log.info("Received W3GS message from " + remote + " with id " + ((AbstractMessage) e.getMessage()).id());
                for (EventListener l : listeners.keySet()) {
                    if (l instanceof GatewayListener) {
                        ((GatewayListener) l).clientMessage(new ClientMessageEvent(W3gsGateway.this, listeners.get(l), remote, (Message) e.getMessage()));
                    }
                }
            }
