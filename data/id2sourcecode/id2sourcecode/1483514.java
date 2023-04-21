    public void testReadWrite() throws Exception {
        final int COUNT = 1024;
        File testFile = env.getTestCaseFile(this);
        FileChannel channel = new RandomAccessFile(testFile, "rw").getChannel();
        testReadWriteImpl(channel, COUNT);
        channel.close();
        channel = new MemoryFileChannel(64 * COUNT);
        testReadWriteImpl(channel, COUNT);
        channel.close();
    }
