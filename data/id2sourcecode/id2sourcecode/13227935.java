    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
        if (msg instanceof HttpResponse && ((HttpResponse) msg).getStatus().getCode() == 100) {
            ctx.sendDownstream(e);
        } else if (msg instanceof HttpMessage) {
            HttpMessage m = (HttpMessage) msg;
            encoder = null;
            String contentEncoding = m.getHeader(HttpHeaders.Names.CONTENT_ENCODING);
            if (contentEncoding != null && !HttpHeaders.Values.IDENTITY.equalsIgnoreCase(contentEncoding)) {
                ctx.sendDownstream(e);
            } else {
                String acceptEncoding = acceptEncodingQueue.poll();
                if (acceptEncoding == null) {
                    throw new IllegalStateException("cannot send more responses than requests");
                }
                boolean hasContent = m.isChunked() || m.getContent().readable();
                if (hasContent && (encoder = newContentEncoder(acceptEncoding)) != null) {
                    m.setHeader(HttpHeaders.Names.CONTENT_ENCODING, getTargetContentEncoding(acceptEncoding));
                    if (!m.isChunked()) {
                        ChannelBuffer content = m.getContent();
                        content = ChannelBuffers.wrappedBuffer(encode(content), finishEncode());
                        m.setContent(content);
                        if (m.containsHeader(HttpHeaders.Names.CONTENT_LENGTH)) {
                            m.setHeader(HttpHeaders.Names.CONTENT_LENGTH, Integer.toString(content.readableBytes()));
                        }
                    }
                }
                ctx.sendDownstream(e);
            }
        } else if (msg instanceof HttpChunk) {
            HttpChunk c = (HttpChunk) msg;
            ChannelBuffer content = c.getContent();
            if (encoder != null) {
                if (!c.isLast()) {
                    content = encode(content);
                    if (content.readable()) {
                        c.setContent(content);
                        ctx.sendDownstream(e);
                    }
                } else {
                    ChannelBuffer lastProduct = finishEncode();
                    if (lastProduct.readable()) {
                        Channels.write(ctx, Channels.succeededFuture(e.getChannel()), new DefaultHttpChunk(lastProduct), e.getRemoteAddress());
                    }
                    ctx.sendDownstream(e);
                }
            } else {
                ctx.sendDownstream(e);
            }
        } else {
            ctx.sendDownstream(e);
        }
    }
