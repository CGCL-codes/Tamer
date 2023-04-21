    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        _logger.debug("configuring test class");
        new File("test-output").mkdir();
        memBuf = ByteBuffer.allocate(STORE_CAPACITY);
        memBuf.put(new byte[STORE_CAPACITY]);
        File deleteFile = new File(STORE_FILE_PATH);
        deleteFile.delete();
        file = new RandomAccessFile(STORE_FILE_PATH, "rw");
        fileBuf = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, STORE_CAPACITY);
        modeRegistry = new StaticModePageRegistry();
        inquiryRegistry = new StaticInquiryDataRegistry();
        memFactory = new BufferedTaskFactory(memBuf, STORE_BLOCK_SIZE, modeRegistry, inquiryRegistry);
        fileFactory = new BufferedTaskFactory(fileBuf, STORE_BLOCK_SIZE, modeRegistry, inquiryRegistry);
    }
