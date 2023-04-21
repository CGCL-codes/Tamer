    public BlockFileChannel(File file, int blockSize, boolean blockCache, ChecksumGenerator checksumGenerator) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            this.fileChannel = raf.getChannel();
            File blockchkFile = new File(getChecksumFilename(file.getName()));
            RandomAccessFile blockchkFileRaf = new RandomAccessFile(blockchkFile, "rw");
            this.blockChecksumChannel = blockchkFileRaf.getChannel();
        } catch (FileNotFoundException e) {
        }
        this.blockSize = blockSize;
        this.blockCache = blockCache;
        this.checksumGenerator = checksumGenerator;
        if (blockCache) {
            cache = new ConcurrentLRUCache<Long, ByteBuffer>(MAX_CAPACITY);
        }
    }
