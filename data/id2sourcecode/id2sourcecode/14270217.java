    public long transferFrom(ReadableByteChannel sourceChannel, int chunkSize) throws IOException, BufferOverflowException {
        long transfered = 0;
        int read = 0;
        do {
            ByteBuffer transferBuffer = ByteBuffer.allocate(chunkSize);
            read = sourceChannel.read(transferBuffer);
            if (read > 0) {
                if (transferBuffer.remaining() == 0) {
                    transferBuffer.flip();
                    write(transferBuffer);
                } else {
                    transferBuffer.flip();
                    write(transferBuffer.slice());
                }
                transfered += read;
            }
        } while (read > 0);
        return transfered;
    }
