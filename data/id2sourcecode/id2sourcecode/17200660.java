    private void initializeWebsocketConnector(final ChannelHandlerContext context, final HttpRequest request, HttpResponse response) {
        String controllerName = request.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_PROTOCOL);
        final DataController controllerUsed = engine.getConfiguration().getDataController(controllerName);
        ChannelFuture future = context.getChannel().write(response);
        future.addListener(new ChannelFutureListener() {

            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    WebsocketConnector<Channel> connector = new NettyConnector(future.getChannel(), engine, controllerUsed);
                    connector.setWebsocketVersion(request.getHeader(ExtendedHttpHeaders.Names.SEC_WEBSOCKET_VERSION));
                    if (future.getChannel().isConnected()) {
                        context.setAttachment(connector);
                        engine.startConnector(connector);
                    }
                }
            }
        });
    }
