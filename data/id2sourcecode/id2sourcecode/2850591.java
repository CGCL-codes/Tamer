    void readFromFile(RandomAccessFile file, ByteBuffer readBuffer, long offset) throws IOException {
        if (useNIO) {
            FileChannel channel = file.getChannel();
            if (chunkedNIOSize > 0) {
                int readLength = readBuffer.limit();
                long currentPosition = offset;
                while (readBuffer.position() < readLength) {
                    readBuffer.limit((int) (Math.min(readBuffer.limit() + chunkedNIOSize, readLength)));
                    int bytesRead = channel.read(readBuffer, currentPosition);
                    if (bytesRead < 1) break;
                    currentPosition += bytesRead;
                }
            } else {
                channel.read(readBuffer, offset);
            }
        } else {
            synchronized (file) {
                assert readBuffer.hasArray();
                assert readBuffer.arrayOffset() == 0;
                int pos = readBuffer.position();
                int size = readBuffer.limit() - pos;
                file.seek(offset);
                int bytesRead = file.read(readBuffer.array(), pos, size);
                if (bytesRead > 0) {
                    readBuffer.position(pos + bytesRead);
                }
            }
        }
    }
