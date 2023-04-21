    protected void setUp() throws Exception {
        super.setUp();
        File tempFile = File.createTempFile("testing", "tmp");
        tempFile.deleteOnExit();
        RandomAccessFile randomAccessFile = new RandomAccessFile(tempFile, "rw");
        readWriteChannel = randomAccessFile.getChannel();
        mockLock = new MockFileLock(readWriteChannel, 10, 100, false);
    }
