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
                            ChannelFuture future = handleRefusedChannel(ctx, e, inetSocketAddress);
                            if (future != null) {
                                future.addListener(ChannelFutureListener.CLOSE);
                            } else {
                                Channels.close(e.getChannel());
                            }
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
