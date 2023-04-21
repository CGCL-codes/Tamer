    public void test_release() throws Exception {
        File file = File.createTempFile("test", "tmp");
        file.deleteOnExit();
        FileOutputStream fout = new FileOutputStream(file);
        FileChannel fileChannel = fout.getChannel();
        FileLock fileLock = fileChannel.lock();
        fileChannel.close();
        try {
            fileLock.release();
            fail("should throw ClosedChannelException");
        } catch (ClosedChannelException e) {
        }
        fout = new FileOutputStream(file);
        fileChannel = fout.getChannel();
        fileLock = fileChannel.lock();
        fileLock.release();
        fileChannel.close();
        try {
            fileLock.release();
            fail("should throw ClosedChannelException");
        } catch (ClosedChannelException e) {
        }
    }
