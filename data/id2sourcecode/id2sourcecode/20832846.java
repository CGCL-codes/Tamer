    public Seg loadSegment() throws IOException {
        File indexFile = getIndexFile();
        if (!indexFile.exists()) return null;
        File entryFile = getEntryFile();
        FileChannel indexFileChannel = new RandomAccessFile(indexFile, "rw").getChannel();
        FileChannel entryFileChannel = new RandomAccessFile(entryFile, "rw").getChannel();
        Seg segment = new Seg(indexFileChannel, entryFileChannel);
        return segment;
    }
