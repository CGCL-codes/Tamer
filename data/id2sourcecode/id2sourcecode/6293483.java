    @Override
    protected Object decode(final ChannelHandlerContext ctx, final Channel channel, final ChannelBuffer in, final DecoderState state) {
        switch(state) {
            case GET_HEADER:
                header = new RtmpHeader(in, incompleteHeaders);
                channelId = header.getChannelId();
                if (incompletePayloads[channelId] == null) {
                    incompleteHeaders[channelId] = header;
                    incompletePayloads[channelId] = ChannelBuffers.buffer(header.getSize());
                }
                payload = incompletePayloads[channelId];
                checkpoint(DecoderState.GET_PAYLOAD);
            case GET_PAYLOAD:
                final byte[] bytes = new byte[Math.min(payload.writableBytes(), chunkSize)];
                in.readBytes(bytes);
                payload.writeBytes(bytes);
                checkpoint(DecoderState.GET_HEADER);
                if (payload.writable()) {
                    return null;
                }
                incompletePayloads[channelId] = null;
                final RtmpHeader prevHeader = completedHeaders[channelId];
                if (!header.isLarge()) {
                    header.setTime(prevHeader.getTime() + header.getDeltaTime());
                }
                final RtmpMessage message = MessageType.decode(header, payload);
                if (logger.isDebugEnabled()) {
                    logger.debug("<< {}", message);
                }
                payload = null;
                if (header.isChunkSize()) {
                    final ChunkSize csMessage = (ChunkSize) message;
                    logger.debug("decoder new chunk size: {}", csMessage);
                    chunkSize = csMessage.getChunkSize();
                }
                completedHeaders[channelId] = header;
                return message;
            default:
                throw new RuntimeException("unexpected decoder state: " + state);
        }
    }
