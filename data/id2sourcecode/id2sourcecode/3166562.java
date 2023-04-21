    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        if (e.getCause() instanceof SSLException) {
            Logger.debug(e.getCause(), "");
            InetSocketAddress inet = ((InetSocketAddress) ctx.getAttachment());
            ctx.getPipeline().remove("ssl");
            HttpResponse nettyResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.TEMPORARY_REDIRECT);
            nettyResponse.setHeader(LOCATION, "https://" + inet.getHostName() + ":" + Server.httpsPort + "/");
            ChannelFuture writeFuture = ctx.getChannel().write(nettyResponse);
            writeFuture.addListener(ChannelFutureListener.CLOSE);
        } else {
            Logger.error(e.getCause(), "");
            e.getChannel().close();
        }
    }
