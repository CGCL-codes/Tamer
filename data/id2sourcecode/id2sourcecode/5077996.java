    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
        HttpMessage currentMessage = this.currentMessage;
        if (msg instanceof HttpMessage) {
            HttpMessage m = (HttpMessage) msg;
            if (is100ContinueExpected(m)) {
                write(ctx, succeededFuture(ctx.getChannel()), CONTINUE.duplicate());
            }
            if (m.isChunked()) {
                List<String> encodings = m.getHeaders(HttpHeaders.Names.TRANSFER_ENCODING);
                encodings.remove(HttpHeaders.Values.CHUNKED);
                if (encodings.isEmpty()) {
                    m.removeHeader(HttpHeaders.Names.TRANSFER_ENCODING);
                }
                m.setChunked(false);
                m.setContent(ChannelBuffers.dynamicBuffer(e.getChannel().getConfig().getBufferFactory()));
                this.currentMessage = m;
            } else {
                this.currentMessage = null;
                ctx.sendUpstream(e);
            }
        } else if (msg instanceof HttpChunk) {
            if (currentMessage == null) {
                throw new IllegalStateException("received " + HttpChunk.class.getSimpleName() + " without " + HttpMessage.class.getSimpleName());
            }
            HttpChunk chunk = (HttpChunk) msg;
            ChannelBuffer content = currentMessage.getContent();
            if (content.readableBytes() > maxContentLength - chunk.getContent().readableBytes()) {
                throw new TooLongFrameException("HTTP content length exceeded " + maxContentLength + " bytes.");
            }
            content.writeBytes(chunk.getContent());
            if (chunk.isLast()) {
                this.currentMessage = null;
                if (chunk instanceof HttpChunkTrailer) {
                    HttpChunkTrailer trailer = (HttpChunkTrailer) chunk;
                    for (Entry<String, String> header : trailer.getHeaders()) {
                        currentMessage.setHeader(header.getKey(), header.getValue());
                    }
                }
                currentMessage.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(content.readableBytes()));
                Channels.fireMessageReceived(ctx, currentMessage, e.getRemoteAddress());
            }
        } else {
            ctx.sendUpstream(e);
        }
    }
