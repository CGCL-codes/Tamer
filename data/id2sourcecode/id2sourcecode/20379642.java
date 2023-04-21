    @Override
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        if (!readingChunks) {
            request = (HttpRequest) e.getMessage();
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Request URI accessed: " + request.getUri() + " channel " + e.getChannel());
            }
            Channel associatedChannel = HttpChannelAssociations.channels.get(e.getChannel());
            InvocationContext invocationContext = balancerRunner.getLatestInvocationContext();
            SIPNode node = null;
            try {
                node = invocationContext.balancerAlgorithm.processHttpRequest(request);
            } catch (Exception ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                logger.log(Level.WARNING, "Problem in balancer algorithm", ex);
                writeResponse(e, HttpResponseStatus.INTERNAL_SERVER_ERROR, "Load Balancer Error: Exception in the balancer algorithm:\n" + sw.toString());
                return;
            }
            if (node == null) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.log(Level.INFO, "Service unavailable. No server is available.");
                }
                writeResponse(e, HttpResponseStatus.SERVICE_UNAVAILABLE, "Service is temporarily unavailable");
                return;
            }
            if (associatedChannel != null && associatedChannel.isConnected()) {
                associatedChannel.write(request);
            } else {
                e.getChannel().getCloseFuture().addListener(new ChannelFutureListener() {

                    public void operationComplete(ChannelFuture arg0) throws Exception {
                        closeChannelPair(arg0.getChannel());
                    }
                });
                ChannelFuture future = HttpChannelAssociations.inboundBootstrap.connect(new InetSocketAddress(node.getIp(), (Integer) node.getProperties().get("httpPort")));
                future.addListener(new ChannelFutureListener() {

                    public void operationComplete(ChannelFuture arg0) throws Exception {
                        Channel channel = arg0.getChannel();
                        HttpChannelAssociations.channels.put(e.getChannel(), channel);
                        HttpChannelAssociations.channels.put(channel, e.getChannel());
                        if (request.isChunked()) {
                            readingChunks = true;
                        }
                        channel.write(request);
                        channel.getCloseFuture().addListener(new ChannelFutureListener() {

                            public void operationComplete(ChannelFuture arg0) throws Exception {
                                closeChannelPair(arg0.getChannel());
                            }
                        });
                    }
                });
            }
        } else {
            HttpChunk chunk = (HttpChunk) e.getMessage();
            if (chunk.isLast()) {
                readingChunks = false;
            }
            HttpChannelAssociations.channels.get(e.getChannel()).write(chunk);
        }
    }
