    protected void sendRequest(final MessageEvent e, final DNSMessage original, final ClientBootstrap bootstrap, final List<SocketAddress> forwarders) {
        if (0 < forwarders.size()) {
            SocketAddress sa = forwarders.remove(0);
            LOG.debug("send to {}", sa);
            ChannelFuture f = bootstrap.connect(sa);
            ChannelBuffer newone = ChannelBuffers.buffer(512);
            DNSMessage msg = new DNSMessage(original);
            msg.write(newone);
            newone.resetReaderIndex();
            final Channel c = f.getChannel();
            if (LOG.isDebugEnabled()) {
                LOG.debug("STATUS : [isOpen/isConnected/isWritable {}] {} {}", new Object[] { new boolean[] { c.isOpen(), c.isConnected(), c.isWritable() }, c.getRemoteAddress(), c.getClass() });
            }
            c.write(newone, sa).addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    LOG.debug("request complete isSuccess : {}", future.isSuccess());
                    if (future.isSuccess() == false) {
                        if (0 < forwarders.size()) {
                            sendRequest(e, original, bootstrap, forwarders);
                        } else {
                            original.header().rcode(RCode.ServFail);
                            ChannelBuffer buffer = ChannelBuffers.buffer(512);
                            original.write(buffer);
                            e.getChannel().write(buffer).addListener(ChannelFutureListener.CLOSE);
                        }
                    }
                }
            });
        }
    }
