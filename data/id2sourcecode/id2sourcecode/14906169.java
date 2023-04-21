    public SequentialRecordCache(File cacheDir, int recordSize, long maxRecords) throws IOException {
        super(cacheDir, maxRecords);
        this.recordSize = recordSize;
        raf = new RandomAccessFile(cacheFile, "rw");
        fileChannel = raf.getChannel();
    }
