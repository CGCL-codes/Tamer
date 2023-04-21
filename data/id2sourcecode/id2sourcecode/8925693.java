    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (!(e.getMessage() instanceof HttpRequest)) {
            throw new Exception("expect HttpRequest but got " + e.getMessage().getClass());
        }
        HttpRequest req = (HttpRequest) e.getMessage();
        HttpProxyRequest request = new HttpProxyRequest(req);
        String remoteAddr = ((InetSocketAddress) e.getRemoteAddress()).getHostName();
        request.setRemoteAddr(remoteAddr);
        ServerNode sn = this.balance.getServer(request);
        assert (sn != null);
        HttpResponse response = new HttpRequestExecutor().execute(sn, req, remoteAddr);
        ChannelFuture f = e.getChannel().write(response);
        f.addListener(ChannelFutureListener.CLOSE);
    }
