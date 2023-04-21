    public long growFile(final long aPreferedCount, final long aMinimumCount) throws IOException {
        if (this.myFileChannel == null) {
            this.myFileChannel = new RandomAccessFile(getFileName(), "rw").getChannel();
        }
        WeakReference<MappedByteBuffer> isGarbageCollected = new WeakReference<MappedByteBuffer>(this.memoryMapped);
        this.memoryMapped = null;
        long start = System.currentTimeMillis();
        System.gc();
        System.runFinalization();
        int i = 2 * 2;
        LOG.info("Growing File - forced Garbage collection done after " + (System.currentTimeMillis() - start) + "ms...");
        while (isGarbageCollected.get() != null) {
            LOG.info("Growing File - memory-mapping not yet garbage-collected");
            System.gc();
            System.runFinalization();
            Thread.yield();
            try {
                final int milliseconds = 1000;
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                LOG.info("sleep interrupted while waiting for finalization");
            }
            i--;
            if (i == 0) {
                LOG.info("Growing File - memory-mapping not yet garbage-collected - GOING ON ANYWAY");
                break;
            }
        }
        isGarbageCollected = null;
        LOG.info("Growing File - old memory collected, mapping new memory...");
        try {
            if (this.memoryMapped != null || this.memoryMappedFirstByte != 0) {
                try {
                    this.memoryMapped = this.myFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, (getRecordCount() + aPreferedCount) * getRecordLength());
                    this.memoryMappedFirstByte = 0;
                    setRecordCount(getRecordCount() + (long) aPreferedCount);
                    return aPreferedCount;
                } catch (IOException e) {
                    LOG.log(Level.INFO, "could not map " + (((long) getRecordCount() + (long) aPreferedCount) * getRecordLength()) + " bytes into memory - " + "trying to map only a part of the file");
                } catch (IllegalArgumentException e) {
                    LOG.log(Level.INFO, "could not map " + (((long) getRecordCount() + (long) aPreferedCount) * getRecordLength()) + " bytes into memory - " + "trying to map only a part of the file");
                }
            }
            this.memoryMapped = null;
            this.memoryMappedFirstByte = getRecordCount() * getRecordLength();
            this.memoryMapped = this.myFileChannel.map(FileChannel.MapMode.READ_WRITE, this.memoryMappedFirstByte, aPreferedCount * getRecordLength());
            setRecordCount(getRecordCount() + (long) aPreferedCount);
            return aPreferedCount;
        } catch (Exception x) {
            this.memoryMapped = null;
            LOG.log(Level.WARNING, "could not map " + (((long) getRecordCount() + (long) aPreferedCount) * getRecordLength()) + " bytes into memory - continuing with conventional IO");
        }
        this.myFileChannel.position((getRecordCount() + aMinimumCount) * (long) getRecordLength() - 1);
        this.myFileChannel.write(ByteBuffer.allocate(1));
        setRecordCount(getRecordCount() + aMinimumCount);
        return aMinimumCount;
    }
