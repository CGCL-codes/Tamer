    public final synchronized void addBytes(BigHash hash, boolean isMetaData, byte[] bytes, int recursionCount) throws Exception {
        final String blockPath = ddc.getDirectoryFile().getAbsolutePath() + filename;
        if (recursionCount >= 100 && isMerging()) {
            throw new Exception("Cannot add bytes; still merging, tried " + recursionCount + " times for " + blockPath);
        }
        if (isMerging()) {
            Thread.sleep(50);
            ddc.dbu.getDataBlockToAddChunk(hash).addBytes(hash, isMetaData, bytes, recursionCount + 1);
            return;
        }
        byte[] buf = new byte[getBytesToRead()];
        lazyCreateFile(buf);
        long bytesUsed = 0;
        long bytesWasted = 0;
        final byte isMetaDataByte = isMetaData ? META_DATA : DATA;
        int nextValidOffset = getBytesToRead();
        RandomAccessFile ras = new RandomAccessFile(blockPath, "rw");
        try {
            fillWithBytes(buf, ras, blockPath, "Reading in header for data block to add " + (isMetaData ? "meta data" : "data") + " chunk.");
            int totalEntriesRead = 0;
            for (int i = 0; i < getHeadersPerFile(); i++) {
                totalEntriesRead++;
                int offset = i * bytesPerEntry;
                BigHash h = BigHash.createFromBytes(buf, offset);
                byte type = buf[offset + BigHash.HASH_LENGTH];
                byte status = buf[offset + BigHash.HASH_LENGTH + 1];
                int o = buf[BigHash.HASH_LENGTH + offset + 2] << 24 | (buf[BigHash.HASH_LENGTH + offset + 2 + 1] & 0xff) << 16 | (buf[BigHash.HASH_LENGTH + offset + 2 + 2] & 0xff) << 8 | (buf[BigHash.HASH_LENGTH + offset + 2 + 3] & 0xff);
                int s = buf[BigHash.HASH_LENGTH + offset + 6] << 24 | (buf[BigHash.HASH_LENGTH + offset + 6 + 1] & 0xff) << 16 | (buf[BigHash.HASH_LENGTH + offset + 6 + 2] & 0xff) << 8 | (buf[BigHash.HASH_LENGTH + offset + 6 + 3] & 0xff);
                if (h.equals(hash)) {
                    if (type == isMetaDataByte) {
                        ras.seek(i * bytesPerEntry + BigHash.HASH_LENGTH + 1);
                        ras.write(STATUS_DELETED);
                        status = STATUS_DELETED;
                    }
                }
                if (status == STATUS_OK) {
                    bytesUsed += s;
                } else {
                    bytesWasted += s;
                }
                if (o != 0) {
                    nextValidOffset = o + s;
                    continue;
                }
                ras.seek(nextValidOffset);
                ras.write(bytes);
                byte[] headerBuf = new byte[bytesPerEntry];
                System.arraycopy(hash.toByteArray(), 0, headerBuf, 0, BigHash.HASH_LENGTH);
                headerBuf[BigHash.HASH_LENGTH] = isMetaDataByte;
                headerBuf[BigHash.HASH_LENGTH + 1] = STATUS_OK;
                headerBuf[BigHash.HASH_LENGTH + 2] = (byte) (nextValidOffset >> 24);
                headerBuf[BigHash.HASH_LENGTH + 2 + 1] = (byte) (nextValidOffset >> 16);
                headerBuf[BigHash.HASH_LENGTH + 2 + 2] = (byte) (nextValidOffset >> 8);
                headerBuf[BigHash.HASH_LENGTH + 2 + 3] = (byte) (nextValidOffset);
                headerBuf[BigHash.HASH_LENGTH + 6] = (byte) (bytes.length >> 24);
                headerBuf[BigHash.HASH_LENGTH + 6 + 1] = (byte) (bytes.length >> 16);
                headerBuf[BigHash.HASH_LENGTH + 6 + 2] = (byte) (bytes.length >> 8);
                headerBuf[BigHash.HASH_LENGTH + 6 + 3] = (byte) (bytes.length);
                ras.seek(offset);
                ras.write(headerBuf);
                ddc.adjustUsedSpace(bytes.length);
                bytesUsed += bytes.length;
                boolean tooManyBytes = ras.length() > DataBlock.getMaxBlockSize();
                boolean tooManyHeaders = i >= DataBlock.getHeadersPerFile() - 1;
                boolean tooMuchWastedSpace = bytesWasted > MAX_WASTED_SPACE_ALLOWED;
                if (!tooMuchWastedSpace && !tooManyBytes && !tooManyHeaders) {
                    return;
                }
                boolean tooManyBytesAdjusted = (ras.length() - bytesWasted) > DataBlock.getMaxBlockSize();
                boolean dontSplitBlock = tooMuchWastedSpace && !tooManyHeaders && !tooManyBytesAdjusted;
                cleanUpDataBlock(dontSplitBlock);
                return;
            }
            try {
                cleanUpDataBlock(false);
            } catch (Exception ex) {
                System.err.println(ex.getClass().getSimpleName() + " while cleaning up data block (recursionCount=" + recursionCount + "): " + ex.getMessage());
                ex.printStackTrace(System.err);
            }
            if (recursionCount <= 3) {
                ddc.dbu.getDataBlockToAddChunk(hash).addBytes(hash, isMetaData, bytes, recursionCount + 1);
            } else {
                throw new Exception("Can't write bytes to this block. Block is full! and recursionCount is " + recursionCount + ": " + blockPath + " <total entries read: " + totalEntriesRead + ", size of file: " + ras.length() + ">");
            }
        } finally {
            ras.close();
        }
    }
