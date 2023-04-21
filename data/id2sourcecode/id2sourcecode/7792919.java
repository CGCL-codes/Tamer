    public FilePipe(@Nullable final SelectorProvider provider, final File file, final boolean deleteFile, final long initialCapacity, final boolean keepContent) throws IOException {
        super();
        CheckArg.notNull(file, "file");
        CheckArg.notNegative(initialCapacity, "initialCapacity");
        boolean createdNewFile = false;
        if (!file.isFile()) {
            if (file.createNewFile()) createdNewFile = true; else if (!file.isFile()) throw new FileNotFoundException("Unable to create file: " + file);
        }
        RandomAccessFile rf = new RandomAccessFile(file, "rw");
        try {
            FileChannel fileChannel = rf.getChannel();
            FileLock lock = fileChannel.tryLock();
            if (lock == null) throw new IOException("Unable to lock file: " + file);
            final long size = fileChannel.size();
            if (keepContent) {
                this.initialSize = size;
                fileChannel.position(size);
            } else {
                this.initialSize = 0;
            }
            if ((initialCapacity > 0) && (size < initialCapacity)) rf.setLength(initialCapacity);
            this.sink = new SinkChannel(rf, provider, this.initialSize);
            this.source = new SourceChannel(file, deleteFile, fileChannel, this.sink);
            rf = null;
        } finally {
            if (rf != null) {
                try {
                    rf.close();
                } catch (final Throwable ex) {
                }
                rf = null;
                if (createdNewFile) {
                    try {
                        file.delete();
                    } catch (final Throwable ex) {
                    }
                }
            }
        }
    }
