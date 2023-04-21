    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "write", args = { int.class }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "read", args = {  }) })
    public void test_read_write() throws IOException {
        int i;
        byte[] testBuf = testString.getBytes();
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        for (i = 0; i < testString.length(); i++) {
            try {
                raf.write(testBuf[i]);
            } catch (Exception e) {
                fail("Test 1: Unexpected exception while writing: " + e.getMessage());
            }
        }
        raf.seek(0);
        for (i = 0; i < testString.length(); i++) {
            assertEquals(String.format("Test 2: Incorrect value written or read at index %d; ", i), testBuf[i], raf.read());
        }
        assertTrue("Test 3: End of file indicator (-1) expected.", raf.read() == -1);
        raf.close();
        try {
            raf.write(42);
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.read();
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
        }
    }
