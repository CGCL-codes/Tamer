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
