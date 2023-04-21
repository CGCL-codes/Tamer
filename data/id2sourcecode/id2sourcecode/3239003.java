    public static String computeHash(File file) throws IOException {
        long size = file.length();
        long chunkSizeForFile = Math.min(HASH_CHUNK_SIZE, size);
        FileChannel fileChannel = new FileInputStream(file).getChannel();
        try {
            long head = computeHashForChunk(fileChannel.map(MapMode.READ_ONLY, 0, chunkSizeForFile));
            long tail = computeHashForChunk(fileChannel.map(MapMode.READ_ONLY, Math.max(size - HASH_CHUNK_SIZE, 0), chunkSizeForFile));
            return String.format("%016x", size + head + tail);
        } finally {
            fileChannel.close();
        }
    }
