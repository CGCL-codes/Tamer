    public ChannelBuffer getChunk(int length) throws IOException {
        if (file == null || length == 0) {
            return ChannelBuffers.EMPTY_BUFFER;
        }
        if (fileChannel == null) {
            FileInputStream inputStream = new FileInputStream(file);
            fileChannel = inputStream.getChannel();
        }
        int read = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        while (read < length) {
            int readnow = fileChannel.read(byteBuffer);
            if (readnow == -1) {
                fileChannel.close();
                fileChannel = null;
                break;
            } else {
                read += readnow;
            }
        }
        if (read == 0) {
            return ChannelBuffers.EMPTY_BUFFER;
        }
        byteBuffer.flip();
        ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(byteBuffer);
        buffer.readerIndex(0);
        buffer.writerIndex(read);
        return buffer;
    }
