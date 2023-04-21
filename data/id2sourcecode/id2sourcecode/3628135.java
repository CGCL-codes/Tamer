    public HashTree readHashTree(HashInfo hashInfo) throws IOException {
        HashTree hashTree = new HashTree(hashInfo);
        seek(hashInfo.getIndex());
        ByteBuffer buffer = ByteBuffer.allocateDirect(HashTree.HASH_SIZE);
        FileChannel fileChannel = getChannel();
        for (int i = 0; i < hashInfo.getLeafNumber(); i++) {
            FileLock lock = fileChannel.lock();
            fileChannel.read(buffer);
            lock.release();
            hashTree.addLeaf(buffer);
        }
        FileLock lock = fileChannel.lock();
        fileChannel.read(buffer);
        lock.release();
        hashTree.setRoot(buffer);
        return hashTree;
    }
