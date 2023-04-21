        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            Object obj = e.getMessage();
            boolean writeEndBuffer = false;
            Object messageToWrite = null;
            if (obj instanceof HttpResponse) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Session[" + relaySession.getID() + "] received direct HTTP response:" + obj);
                }
                HttpResponse response = (HttpResponse) obj;
                String te = response.getHeader(HttpHeaders.Names.TRANSFER_ENCODING);
                if (null != te) {
                    te = te.trim();
                }
                if (response.isChunked()) {
                    writeEndBuffer = false;
                } else {
                    writeEndBuffer = true;
                }
                messageToWrite = response;
                keepAlive = HttpHeaders.isKeepAlive(response);
                closeEndsResponseBody = closeEndsResponseBody(response);
            } else if (obj instanceof HttpChunk) {
                HttpChunk chunk = (HttpChunk) obj;
                messageToWrite = chunk;
                if (chunk.isLast()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Session[" + relaySession.getID() + "] received direct last HTTP chunk.");
                    }
                    writeEndBuffer = true;
                } else {
                    writeEndBuffer = false;
                }
            } else if (obj instanceof ChannelBuffer) {
                ChannelBuffer buf = (ChannelBuffer) obj;
                relaySession.writeLocal(buf, ctx.getChannel());
                return;
            } else {
                logger.error("Unexpected message type:" + obj.getClass().getName());
                return;
            }
            if (null != messageToWrite) {
                relaySession.writeLocal(messageToWrite, ctx.getChannel());
                if (writeEndBuffer) {
                    unanwsered = false;
                    relaySession.writeLocal(ChannelBuffers.EMPTY_BUFFER, ctx.getChannel());
                    relaySession.transactionCompelete(ctx.getChannel());
                    if (keepAlive) {
                        onChannelAvailable(remoteAddress, remoteChannelFuture);
                    } else {
                        relaySession.closeRemote(ctx.getChannel(), remoteAddress);
                    }
                    if (closeEndsResponseBody) {
                        relaySession.closeLocalChannel();
                    }
                }
            }
        }
