    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent) {
            ChannelStateEvent evt = (ChannelStateEvent) e;
            switch(evt.getState()) {
                case OPEN:
                case BOUND:
                    if (isBlocked(ctx) && !continues(ctx, evt)) {
                        return;
                    } else {
                        ctx.sendUpstream(e);
                        return;
                    }
                case CONNECTED:
                    if (evt.getValue() != null) {
                        InetSocketAddress inetSocketAddress = (InetSocketAddress) e.getChannel().getRemoteAddress();
                        if (!accept(ctx, e, inetSocketAddress)) {
                            ctx.setAttachment(Boolean.TRUE);
                            final ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
                            buffer.writeBytes("/".getBytes());
                            buffer.writeBytes("ok".getBytes());
                            ChannelFuture future = e.getChannel().write(buffer);
                            future.addListener(ChannelFutureListener.CLOSE);
                            LOG.info("Ignoring " + inetSocketAddress.getAddress().getHostAddress());
                            if (isBlocked(ctx) && !continues(ctx, evt)) {
                                return;
                            }
                        }
                        ctx.setAttachment(null);
                    } else {
                        if (isBlocked(ctx) && !continues(ctx, evt)) {
                            return;
                        }
                    }
                    break;
            }
        }
        if (isBlocked(ctx) && !continues(ctx, e)) {
            return;
        }
        ctx.sendUpstream(e);
    }
