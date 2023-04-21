    public void test_lock() throws Exception {
        FileChannel fc = null, anotherfc = null;
        String testFileName = "testLockAndTryLock";
        File f = File.createTempFile(testFileName, "txt");
        f.deleteOnExit();
        FileOutputStream out = new FileOutputStream(f);
        out.write(1);
        out.close();
        fc = new RandomAccessFile(f, "rw").getChannel();
        anotherfc = new RandomAccessFile(f, "rw").getChannel();
        assertNotNull(fc.lock());
        ByteBuffer readBuf = ByteBuffer.allocate(100);
        ByteBuffer writeBuf = ByteBuffer.wrap("bytes".getBytes());
        try {
            try {
                anotherfc.read(readBuf);
                fail("should throw IOException.");
            } catch (IOException e) {
            }
            try {
                anotherfc.read(new ByteBuffer[] { readBuf }, 0, 1);
                fail("should throw IOException.");
            } catch (IOException e) {
            }
            try {
                anotherfc.write(writeBuf);
                fail("should throw IOException.");
            } catch (IOException e) {
            }
            try {
                anotherfc.write(new ByteBuffer[] { writeBuf }, 0, 1);
                fail("should throw IOException.");
            } catch (IOException e) {
            }
        } finally {
            fc.close();
            anotherfc.close();
        }
    }
