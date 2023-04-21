    public static String getOsdbHash(File file) throws IOException {
        long size = file.length();
        long chunkSizeForFile = Math.min(HASH_CHUNK_SIZE, size);
        FileChannel fileChannel = new FileInputStream(file).getChannel();
        long head = computeHashForChunk(fileChannel, 0, chunkSizeForFile);
        long tail = computeHashForChunk(fileChannel, Math.max(size - HASH_CHUNK_SIZE, 0), chunkSizeForFile);
        fileChannel.close();
        return String.format("%016x", size + head + tail);
    }
