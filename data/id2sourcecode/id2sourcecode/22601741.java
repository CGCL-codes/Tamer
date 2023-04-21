    public ByteBuffer encode(final int chunkSize) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        header.encode(buffer);
        int remaining = header.getSize();
        while (true) {
            final int toWrite = remaining > chunkSize ? chunkSize : remaining;
            byte[] bytes = new byte[toWrite];
            data.get(bytes);
            buffer.put(bytes);
            remaining -= chunkSize;
            if (remaining > 0) {
                Header tiny = new Header(TINY, header.getChannelId(), header.getPacketType());
                tiny.encode(buffer);
            } else {
                break;
            }
        }
        buffer.flip();
        return buffer;
    }
