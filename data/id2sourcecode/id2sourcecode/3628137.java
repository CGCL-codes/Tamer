    public HashInfo writeHashTree(HashTree hashTree, long position) throws IOException {
        HashInfo hashInfo = hashTree.getHashInfo();
        long index = position;
        if (position > length()) {
            throw new IOException("Position bigger than file length");
        }
        seek(index);
        FileChannel fileChannel = getChannel();
        List leaves = hashTree.getLeaves();
        Iterator iterator = leaves.iterator();
        ByteBuffer buffer;
        while (iterator.hasNext()) {
            buffer = (ByteBuffer) iterator.next();
            buffer.position(0);
            FileLock lock = fileChannel.lock();
            fileChannel.write(buffer);
            lock.release();
            index += HashTree.HASH_SIZE;
            seek(index);
        }
        FileLock lock = fileChannel.lock();
        fileChannel.write(hashTree.getRoot());
        lock.release();
        return hashInfo;
    }
