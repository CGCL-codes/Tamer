        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            HttpRequest _request = (HttpRequest) e.getMessage();
            WORequest worequest = asWORequest(_request);
            worequest._setOriginatingAddress(((InetSocketAddress) ctx.getChannel().getRemoteAddress()).getAddress());
            WOResponse woresponse = WOApplication.application().dispatchRequest(worequest);
            NSDelayedCallbackCenter.defaultCenter().eventEnded();
            boolean keepAlive = isKeepAlive(_request);
            ChannelFuture future = e.getChannel().write(asHttpResponse(woresponse));
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
