        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            if (!readingChunks) {
                HttpResponse response = (HttpResponse) e.getMessage();
                if (logger.isDebugEnabled()) {
                    logger.debug("Recv response:" + e.getMessage());
                }
                if (casSSLProxyConnectionStatus(WAITING_CONNECT_RESPONSE, CONNECT_RESPONSED)) {
                    HttpMessageDecoder decoder = e.getChannel().getPipeline().get(HttpResponseDecoder.class);
                    Method m = HttpMessageDecoder.class.getDeclaredMethod("reset", null);
                    m.setAccessible(true);
                    m.invoke(decoder, null);
                    waitingResponse.set(false);
                    if (null != sslChannelInitTask) {
                        sslChannelInitTask.onVerify();
                        sslChannelInitTask = null;
                    }
                    return;
                }
                responseContentLength = (int) HttpHeaders.getContentLength(response);
                if (response.getStatus().getCode() == 200) {
                    if (response.isChunked()) {
                        readingChunks = true;
                        waitingResponse.set(true);
                    } else {
                        readingChunks = false;
                        waitingResponse.set(false);
                    }
                    ChannelBuffer content = response.getContent();
                    fillResponseBuffer(content);
                } else {
                    waitingResponse.set(false);
                    logger.error("Received error response:" + response);
                    closeRelevantSessions(response);
                }
            } else {
                HttpChunk chunk = (HttpChunk) e.getMessage();
                if (chunk.isLast()) {
                    readingChunks = false;
                    waitingResponse.set(false);
                }
                fillResponseBuffer(chunk.getContent());
            }
        }
