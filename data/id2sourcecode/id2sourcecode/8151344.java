    public static void truncateBaseSegment(File indexFile, File entryFile, int entryCount) throws IOException {
        FileChannel indexChannel = new RandomAccessFile(indexFile, "rw").getChannel();
        FileChannel entryChannel = new RandomAccessFile(entryFile, "rw").getChannel();
        BaseSegment baseSegment = new BaseSegment(indexChannel, entryChannel);
        baseSegment.truncateEntryCount(entryCount);
        baseSegment.close();
    }
