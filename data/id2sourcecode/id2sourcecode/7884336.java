    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getChannel", args = {  })
    public void test_getChannel() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        FileChannel fc = raf.getChannel();
        assertTrue("Test 1: Channel position expected to be 0.", fc.position() == 0);
        raf.write(testString.getBytes());
        assertEquals("Test 2: Unexpected channel position.", testLength, fc.position());
        assertTrue("Test 3: Channel position is not equal to file pointer.", fc.position() == raf.getFilePointer());
        raf.close();
    }
