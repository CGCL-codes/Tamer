    private void flushLiveCache() throws IOException {
        if (this.fileCacheLineCount < this.liveCacheLineEnd) {
            if (this.fileCacheName == null) {
                this.fileCacheName = File.createTempFile("fs-text-data-cache-", null);
                this.fileCache = new RandomAccessFile(this.fileCacheName, "rw");
            }
            final FileChannel fileCacheChannel = this.fileCache.getChannel();
            final long writePosition = fileCacheChannel.size();
            this.fileCacheIndex.put(this.fileCacheLineCount, writePosition);
            final ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
            final ByteBuffer typeBuffer = ByteBuffer.allocate(1);
            lengthBuffer.putInt(0, this.liveCache.size());
            safeWrite(fileCacheChannel, lengthBuffer);
            for (final TextData[] line : this.liveCache) {
                lengthBuffer.clear();
                lengthBuffer.putInt(0, line.length);
                safeWrite(fileCacheChannel, lengthBuffer);
                for (final TextData element : line) {
                    final ByteBuffer textBuffer = ByteBuffer.wrap(element.text().getBytes());
                    lengthBuffer.clear();
                    lengthBuffer.putInt(0, textBuffer.capacity());
                    safeWrite(fileCacheChannel, lengthBuffer);
                    safeWrite(fileCacheChannel, textBuffer);
                    typeBuffer.clear();
                    switch(element.type()) {
                        case VALUE:
                            typeBuffer.put(0, (byte) 0);
                            break;
                        case SYMBOL:
                            typeBuffer.put(0, (byte) 1);
                            break;
                        case KEYWORD:
                            typeBuffer.put(0, (byte) 2);
                            break;
                        case OPERATOR:
                            typeBuffer.put(0, (byte) 3);
                            break;
                        case LABEL:
                            typeBuffer.put(0, (byte) 4);
                            break;
                        case COMMENT:
                            typeBuffer.put(0, (byte) 5);
                            break;
                        case ERROR:
                            typeBuffer.put(0, (byte) 6);
                            break;
                    }
                    safeWrite(fileCacheChannel, typeBuffer);
                }
            }
            this.fileCacheLineCount += this.liveCache.size();
        }
    }
