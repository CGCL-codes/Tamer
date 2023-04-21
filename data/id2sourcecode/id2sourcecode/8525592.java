    public ChannelBuffer getNetworkPacket() {
        final ChannelBuffer buf = ChannelBuffers.dynamicBuffer(13);
        buf.writeInt(buffer.readableBytes() + 9);
        buf.writeInt(remoteId);
        buf.writeInt(localId);
        buf.writeByte(code);
        return ChannelBuffers.wrappedBuffer(buf, buffer);
    }
