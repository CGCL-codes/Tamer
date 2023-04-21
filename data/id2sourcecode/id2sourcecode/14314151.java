    private static ByteBuffer readFile(final File file) throws IOException {
        final FileChannel channel = new FileInputStream(file).getChannel();
        final long bytesTotal = channel.size();
        final ByteBuffer buffer = ByteBuffer.allocateDirect((int) bytesTotal);
        long bytesRead = 0;
        do {
            bytesRead += channel.read(buffer);
        } while (bytesRead < bytesTotal);
        buffer.flip();
        channel.close();
        return buffer;
    }
