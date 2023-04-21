    public ByteBuffer encodePacket(RTMP rtmp, Packet packet) {
        final Header header = packet.getHeader();
        final byte channelId = header.getChannelId();
        final IRTMPEvent message = packet.getMessage();
        ByteBuffer data;
        if (message instanceof ChunkSize) {
            ChunkSize chunkSizeMsg = (ChunkSize) message;
            rtmp.setWriteChunkSize(chunkSizeMsg.getSize());
        }
        try {
            data = encodeMessage(header, message);
        } finally {
            message.release();
        }
        if (data.position() != 0) {
            data.flip();
        } else {
            data.rewind();
        }
        header.setSize(data.limit());
        final ByteBuffer headers = encodeHeader(header, rtmp.getLastWriteHeader(channelId));
        rtmp.setLastWriteHeader(channelId, header);
        rtmp.setLastWritePacket(channelId, packet);
        final int chunkSize = rtmp.getWriteChunkSize();
        final int numChunks = (int) Math.ceil(header.getSize() / (float) chunkSize);
        final int bufSize = header.getSize() + headers.limit() + (numChunks - 1 * 1);
        final ByteBuffer out = ByteBuffer.allocate(bufSize);
        headers.flip();
        out.put(headers);
        headers.release();
        if (numChunks == 1) {
            BufferUtils.put(out, data, out.remaining());
        } else {
            for (int i = 0; i < numChunks - 1; i++) {
                BufferUtils.put(out, data, chunkSize);
                out.put(RTMPUtils.encodeHeaderByte(HEADER_CONTINUE, header.getChannelId()));
            }
            BufferUtils.put(out, data, out.remaining());
        }
        data.release();
        out.flip();
        return out;
    }
