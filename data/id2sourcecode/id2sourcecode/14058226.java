    public void setContent(ChannelBuffer buffer) throws IOException {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        size = buffer.readableBytes();
        if (definedSize > 0 && definedSize < size) {
            throw new IOException("Out of size: " + size + " > " + definedSize);
        }
        if (file == null) {
            file = tempFile();
        }
        if (buffer.readableBytes() == 0) {
            file.createNewFile();
            return;
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        FileChannel localfileChannel = outputStream.getChannel();
        ByteBuffer byteBuffer = buffer.toByteBuffer();
        int written = 0;
        while (written < size) {
            written += localfileChannel.write(byteBuffer);
        }
        buffer.readerIndex(buffer.readerIndex() + written);
        localfileChannel.force(false);
        localfileChannel.close();
        completed = true;
    }
