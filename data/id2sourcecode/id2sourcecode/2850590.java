    private int writeToFile(RandomAccessFile file, ByteBuffer data, long destOffset) throws IOException, DatabaseException {
        int totalBytesWritten = 0;
        if (useNIO) {
            FileChannel channel = file.getChannel();
            if (chunkedNIOSize > 0) {
                ByteBuffer useData = data.duplicate();
                int originalLimit = useData.limit();
                useData.limit(useData.position());
                while (useData.limit() < originalLimit) {
                    useData.limit((int) (Math.min(useData.limit() + chunkedNIOSize, originalLimit)));
                    int bytesWritten = channel.write(useData, destOffset);
                    destOffset += bytesWritten;
                    totalBytesWritten += bytesWritten;
                }
            } else {
                totalBytesWritten = channel.write(data, destOffset);
            }
        } else {
            synchronized (file) {
                assert data.hasArray();
                assert data.arrayOffset() == 0;
                int pos = data.position();
                int size = data.limit() - pos;
                file.seek(destOffset);
                file.write(data.array(), pos, size);
                data.position(pos + size);
                totalBytesWritten = size;
            }
        }
        return totalBytesWritten;
    }
