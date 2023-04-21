    @Override
    public boolean onEncode(Buffer buffer) {
        BufferHelper.writeVarInt(buffer, sequence);
        BufferHelper.writeVarInt(buffer, total);
        BufferHelper.writeVarInt(buffer, content.readableBytes());
        buffer.write(content, content.readableBytes());
        return true;
    }
