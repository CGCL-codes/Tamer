    public CacheFile getFile(int cache, int file) throws IOException {
        if (cache < 0 || cache >= indexFiles.length) {
            throw new IOException("Cache does not exist.");
        }
        RandomAccessFile indexFile = indexFiles[cache];
        cache += 1;
        if (file < 0 || file >= (indexFile.length() * INDEX_SIZE)) {
            throw new IOException("File does not exist.");
        }
        ByteBuffer index = indexFile.getChannel().map(MapMode.READ_ONLY, INDEX_SIZE * file, INDEX_SIZE);
        int fileSize = ((index.get() & 0xFF) << 16) | ((index.get() & 0xFF) << 8) | (index.get() & 0xFF);
        int fileBlock = ((index.get() & 0xFF) << 16) | ((index.get() & 0xFF) << 8) | (index.get() & 0xFF);
        int remainingBytes = fileSize;
        int currentBlock = fileBlock;
        ByteBuffer fileBuffer = ByteBuffer.allocate(fileSize);
        int cycles = 0;
        while (remainingBytes > 0) {
            int size = DATA_SIZE;
            int rem = (int) (dataFile.length() - currentBlock * DATA_SIZE);
            if (rem < DATA_SIZE) {
                size = rem;
            }
            ByteBuffer block = dataFile.getChannel().map(MapMode.READ_ONLY, currentBlock * DATA_SIZE, size);
            int nextFileId = block.getShort() & 0xFFFF;
            int currentPartId = block.getShort() & 0xFFFF;
            int nextBlockId = ((block.get() & 0xFF) << 16) | ((block.get() & 0xFF) << 8) | (block.get() & 0xFF);
            int nextCacheId = block.get() & 0xFF;
            size -= 8;
            int bytesThisCycle = remainingBytes;
            if (bytesThisCycle > DATA_BLOCK_SIZE) {
                bytesThisCycle = DATA_BLOCK_SIZE;
            }
            byte[] temp = new byte[bytesThisCycle];
            block.get(temp);
            fileBuffer.put(temp, 0, bytesThisCycle);
            remainingBytes -= bytesThisCycle;
            if (cycles != currentPartId) {
                throw new IOException("Cycle does not match part id.");
            }
            if (remainingBytes > 0) {
                if (nextCacheId != cache) {
                    throw new IOException("Unexpected next cache id.");
                }
                if (nextFileId != file) {
                    throw new IOException("Unexpected next file id.");
                }
            }
            cycles++;
            currentBlock = nextBlockId;
        }
        return new CacheFile(cache, file, (ByteBuffer) fileBuffer.flip());
    }
