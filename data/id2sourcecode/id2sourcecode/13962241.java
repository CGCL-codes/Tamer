    public HashFile(File file, boolean forceSync) throws IOException {
        this.file = file;
        this.forceSync = forceSync;
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                throw new IOException("Failed to create file: " + file);
            }
        }
        raf = new RandomAccessFile(file, "rw");
        fileChannel = raf.getChannel();
        if (fileChannel.size() == 0L) {
            bucketCount = INIT_BUCKET_COUNT;
            bucketSize = INIT_BUCKET_SIZE;
            itemCount = 0;
            recordSize = ITEM_SIZE * bucketSize + 4;
            writeEmptyBuckets(HEADER_LENGTH, bucketCount);
            sync();
        } else {
            readFileHeader();
            recordSize = ITEM_SIZE * bucketSize + 4;
        }
    }
