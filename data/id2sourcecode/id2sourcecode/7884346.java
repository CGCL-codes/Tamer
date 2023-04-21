    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeDouble", args = { double.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readDouble", args = {  }) })
    public void test_read_writeDouble() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeDouble(Double.MAX_VALUE);
        raf.writeDouble(424242.4242);
        raf.seek(0);
        assertEquals("Test 1: Incorrect value written or read;", Double.MAX_VALUE, raf.readDouble());
        assertEquals("Test 2: Incorrect value written or read;", 424242.4242, raf.readDouble());
        try {
            raf.readDouble();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
        }
        raf.close();
        try {
            raf.writeDouble(Double.MIN_VALUE);
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.readDouble();
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
        }
    }
