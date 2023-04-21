    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ChannelBuffer buff = (ChannelBuffer) e.getMessage();
        ChannelBufferInputStream channelInput = new ChannelBufferInputStream(buff);
        final SyncPointer syncPointer = objMapper.readValue(channelInput, SyncPointer.class);
        if (syncPointer == null) {
            throw new RuntimeException("Please send a SyncPointer object: SyncPointer is null");
        }
        if (syncPointer.getLockId() == null) {
            throw new RuntimeException("Please send a SyncPointer object with a lockId: SyncPointer.getLockId == null");
        }
        final boolean ok = saveAndReleaseLock(syncPointer, (InetSocketAddress) e.getRemoteAddress());
        ChannelBuffer buffer = null;
        if (ok) {
            byte[] okBytes = "OK".getBytes();
            buffer = ChannelBuffers.buffer(okBytes.length + 8);
            buffer.writeInt(okBytes.length + 4);
            buffer.writeInt(200);
            buffer.writeBytes(okBytes);
        } else {
            buffer = ChannelBuffers.buffer(CONFLICT_MESSAGE.length + 8);
            buffer.writeInt(CONFLICT_MESSAGE.length + 4);
            buffer.writeInt(409);
            buffer.writeBytes(CONFLICT_MESSAGE);
        }
        ChannelFuture future = e.getChannel().write(buffer);
        future.addListener(ChannelFutureListener.CLOSE);
    }
