    @Override
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        localChannel = e.getChannel();
        if (e.getMessage() instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) e.getMessage();
            handleHttpRequest(request, e);
        } else {
            handleChunks(e);
        }
    }
