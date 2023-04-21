    public long getChecksum(File file) throws IOException, IllegalStateException {
        long sum = 0;
        long end = file.length();
        try {
            FileTransferChecksum summer = new FileTransferChecksum();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long remaining = end;
            RandomAccessFile aFile = new RandomAccessFile(file, "r");
            FileChannel channel = aFile.getChannel();
            while (remaining > 0) {
                buffer.rewind();
                buffer.limit((int) Math.min(remaining, buffer.capacity()));
                int count = channel.read(buffer);
                if (count == -1) break;
                buffer.flip();
                remaining -= buffer.limit();
                summer.update(buffer.array(), buffer.arrayOffset(), buffer.limit());
            }
            if (remaining > 0) {
                throw new IOException("could not get checksum for entire file; " + remaining + " failed of " + end);
            }
            sum = summer.getValue();
        } finally {
        }
        return sum;
    }
