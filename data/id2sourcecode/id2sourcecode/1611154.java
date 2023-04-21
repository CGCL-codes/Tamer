    private void finishQueuedContens() {
        ChannelBuffer sizeBuf = ChannelBuffers.buffer(4);
        int size = 0;
        ChannelBuffer wrap = null;
        for (Buffer c : queuedContents) {
            size += c.readableBytes();
            ChannelBuffer tmp = ChannelBuffers.wrappedBuffer(c.getRawBuffer(), c.getReadIndex(), c.readableBytes());
            if (null == wrap) {
                wrap = tmp;
            } else {
                wrap = ChannelBuffers.wrappedBuffer(wrap, tmp);
            }
        }
        sizeBuf.writeInt(size);
        final HttpChunk chunk = new DefaultHttpChunk(ChannelBuffers.wrappedBuffer(sizeBuf, wrap));
        transactionChunkSize++;
        if (writeFuture != null && !writeFuture.isDone()) {
            writeFuture.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    writeFuture = getRemoteFuture().getChannel().write(chunk);
                }
            });
        } else {
            writeFuture = getRemoteFuture().getChannel().write(chunk);
        }
        queuedContents.clear();
    }
