    public void encode(Buffer buffer) {
        BufferHelper.writeFixInt32(buffer, domain.length(), true);
        buffer.write(domain.getBytes());
        BufferHelper.writeFixInt32(buffer, content.readableBytes(), true);
        buffer.write(content.getRawBuffer(), content.getReadIndex(), content.readableBytes());
    }
