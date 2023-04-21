    private void updateBufferFromFile() throws IOException {
        if (channel == null) {
            channel = new RandomAccessFile(file, "r").getChannel();
        }
        buffer.clear();
        channel.read(buffer, bufferStart);
        restartCloseTimer();
    }
