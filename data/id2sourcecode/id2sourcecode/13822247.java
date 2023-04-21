    void read(byte[] buffer, int offset) {
        int len = buffer.length;
        DataInputBlock currentBlock = getDataInputBlock(offset);
        int blockAvailable = currentBlock.available();
        if (blockAvailable > len) {
            currentBlock.readFully(buffer, 0, len);
            return;
        }
        int remaining = len;
        int writePos = 0;
        int currentOffset = offset;
        while (remaining > 0) {
            boolean blockIsExpiring = remaining >= blockAvailable;
            int reqSize;
            if (blockIsExpiring) {
                reqSize = blockAvailable;
            } else {
                reqSize = remaining;
            }
            currentBlock.readFully(buffer, writePos, reqSize);
            remaining -= reqSize;
            writePos += reqSize;
            currentOffset += reqSize;
            if (blockIsExpiring) {
                if (currentOffset == _size) {
                    if (remaining > 0) {
                        throw new IllegalStateException("reached end of document stream unexpectedly");
                    }
                    currentBlock = null;
                    break;
                }
                currentBlock = getDataInputBlock(currentOffset);
                blockAvailable = currentBlock.available();
            }
        }
    }
