        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            if (!readingChunks) {
                HttpResponse response = (HttpResponse) e.getMessage();
                int bodyLen = (int) response.getContentLength();
                if (logger.isDebugEnabled()) {
                    logger.debug("Recv message:" + response + " with len:" + bodyLen + " from " + addressTable.get(e.getChannel()));
                }
                if (syncLockTable.containsKey(e.getChannel())) {
                    LockAndCondition lc = syncLockTable.get(e.getChannel());
                    try {
                        lc.lock.lock();
                        lc.responseCode = response.getStatus().getCode();
                        HttpMessageDecoder decoder = e.getChannel().getPipeline().get(HttpResponseDecoder.class);
                        Method m = HttpMessageDecoder.class.getDeclaredMethod("reset", null);
                        m.setAccessible(true);
                        m.invoke(decoder, null);
                        lc.cond.signal();
                    } finally {
                        lc.lock.unlock();
                    }
                    return;
                }
                if (response.getStatus().getCode() == 200 && response.isChunked()) {
                    readingChunks = true;
                } else {
                    if (response.getStatus().equals(HttpResponseStatus.OK) && bodyLen > 0) {
                        content = response.getContent();
                        notifyRpcReader(e.getChannel());
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Recv message with no body or error rsponse" + response);
                        }
                    }
                }
            } else {
                HttpChunk chunk = (HttpChunk) e.getMessage();
                if (chunk.isLast()) {
                    readingChunks = false;
                    notifyRpcReader(e.getChannel());
                } else {
                    ChannelBuffer chunkContent = chunk.getContent();
                    if (null == content) {
                        content = chunkContent;
                    } else {
                        content = ChannelBuffers.wrappedBuffer(content, chunkContent);
                    }
                }
            }
        }
