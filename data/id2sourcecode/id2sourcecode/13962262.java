    private void increaseHashTable() throws IOException {
        long oldTableSize = HEADER_LENGTH + (long) bucketCount * recordSize;
        long newTableSize = HEADER_LENGTH + (long) bucketCount * recordSize * 2;
        long oldFileSize = fileChannel.size();
        File tmpFile = new File(file.getParentFile(), "rehash_" + file.getName());
        RandomAccessFile tmpRaf = createEmptyFile(tmpFile);
        FileChannel tmpChannel = tmpRaf.getChannel();
        fileChannel.transferTo(oldTableSize, oldFileSize, tmpChannel);
        writeEmptyBuckets(oldTableSize, bucketCount);
        bucketCount *= 2;
        fileChannel.truncate(newTableSize);
        ByteBuffer bucket = ByteBuffer.allocate(recordSize);
        ByteBuffer newBucket = ByteBuffer.allocate(recordSize);
        for (long bucketOffset = HEADER_LENGTH; bucketOffset < oldTableSize; bucketOffset += recordSize) {
            fileChannel.read(bucket, bucketOffset);
            boolean bucketChanged = false;
            long newBucketOffset = 0L;
            for (int slotNo = 0; slotNo < bucketSize; slotNo++) {
                int id = bucket.getInt(ITEM_SIZE * slotNo + 4);
                if (id != 0) {
                    int hash = bucket.getInt(ITEM_SIZE * slotNo);
                    long newOffset = getBucketOffset(hash);
                    if (newOffset != bucketOffset) {
                        newBucket.putInt(hash);
                        newBucket.putInt(id);
                        bucket.putInt(ITEM_SIZE * slotNo, 0);
                        bucket.putInt(ITEM_SIZE * slotNo + 4, 0);
                        bucketChanged = true;
                        newBucketOffset = newOffset;
                    }
                }
            }
            if (bucketChanged) {
                newBucket.flip();
                fileChannel.write(newBucket, newBucketOffset);
                newBucket.clear();
            }
            if (bucket.getInt(ITEM_SIZE * bucketSize) != 0) {
                bucket.putInt(ITEM_SIZE * bucketSize, 0);
                bucketChanged = true;
            }
            if (bucketChanged) {
                bucket.rewind();
                fileChannel.write(bucket, bucketOffset);
            }
            bucket.clear();
        }
        long tmpFileSize = tmpChannel.size();
        for (long bucketOffset = 0L; bucketOffset < tmpFileSize; bucketOffset += recordSize) {
            tmpChannel.read(bucket, bucketOffset);
            for (int slotNo = 0; slotNo < bucketSize; slotNo++) {
                int id = bucket.getInt(ITEM_SIZE * slotNo + 4);
                if (id != 0) {
                    int hash = bucket.getInt(ITEM_SIZE * slotNo);
                    long newBucketOffset = getBucketOffset(hash);
                    storeID(newBucketOffset, hash, id);
                    bucket.putInt(ITEM_SIZE * slotNo, 0);
                    bucket.putInt(ITEM_SIZE * slotNo + 4, 0);
                }
            }
            bucket.clear();
        }
        tmpRaf.close();
        tmpFile.delete();
    }
