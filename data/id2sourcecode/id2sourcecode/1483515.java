    public void testTransferFromBoundaryCondition() throws Exception {
        FileChannel channel;
        File testFile = env.getTestCaseFile(this);
        channel = new RandomAccessFile(testFile, "rw").getChannel();
        testTransferFromBoundaryConditionImpl(channel);
        channel.close();
        channel = new MemoryFileChannel(16);
        testTransferFromBoundaryConditionImpl(channel);
        channel.close();
    }
