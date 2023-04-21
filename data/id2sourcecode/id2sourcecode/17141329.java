            @Override
            public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
                InetSocketAddress remote = (InetSocketAddress) e.getChannel().getRemoteAddress();
                if (channelsByAddress.containsKey(remote)) {
                    return;
                }
                log.info("New W3GS connection from " + remote + ".");
                channelsByAddress.put(remote, e.getChannel());
                for (EventListener l : listeners.keySet()) {
                    if (l instanceof GatewayListener) {
                        ((GatewayListener) l).clientConnected(new ClientConnectionEvent(W3gsGateway.this, listeners.get(l), remote));
                    }
                }
            }
