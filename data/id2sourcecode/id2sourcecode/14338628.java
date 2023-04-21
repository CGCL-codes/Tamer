    public long transferFrom(FileChannel fileChannel) throws ClosedChannelException, IOException, SocketTimeoutException, ClosedChannelException {
        ensureStreamIsOpenAndWritable();
        if (getFlushmode() == FlushMode.SYNC) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("tranfering file by using MappedByteBuffer (MAX_MAP_SIZE=" + TRANSFER_BYTE_BUFFER_MAX_MAP_SIZE + ")");
            }
            final long size = fileChannel.size();
            long remaining = size;
            long offset = 0;
            long length = 0;
            do {
                if (remaining > TRANSFER_BYTE_BUFFER_MAX_MAP_SIZE) {
                    length = TRANSFER_BYTE_BUFFER_MAX_MAP_SIZE;
                } else {
                    length = remaining;
                }
                MappedByteBuffer buffer = fileChannel.map(MapMode.READ_ONLY, offset, length);
                long written = write(buffer);
                offset += written;
                remaining -= written;
            } while (remaining > 0);
            return size;
        } else {
            return transferFrom((ReadableByteChannel) fileChannel);
        }
    }
