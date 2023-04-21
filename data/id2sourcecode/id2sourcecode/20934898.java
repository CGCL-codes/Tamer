    public int transferFrom(InputStream in, int len) throws IOException {
        int minCapacity = position + len;
        if (minCapacity > capacity) {
            grow(minCapacity);
        }
        int totalBytesTransferred = 0;
        while (len > 0) {
            int bytesTransferred = in.read(buffer, position, len);
            if (bytesTransferred == -1) {
                len = 0;
            } else {
                totalBytesTransferred += bytesTransferred;
                position += bytesTransferred;
                len -= bytesTransferred;
            }
        }
        if (position > size) {
            size = position;
        }
        return totalBytesTransferred;
    }
