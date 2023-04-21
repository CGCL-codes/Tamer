    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeFloat", args = { float.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readFloat", args = {  }) })
    public void test_read_writeFloat() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeFloat(Float.MAX_VALUE);
        raf.writeFloat(555.55f);
        raf.seek(0);
        assertEquals("Test 1: Incorrect value written or read. ", Float.MAX_VALUE, raf.readFloat());
        assertEquals("Test 2: Incorrect value written or read. ", 555.55f, raf.readFloat());
        try {
            raf.readFloat();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
        }
        raf.close();
        try {
            raf.writeFloat(Float.MIN_VALUE);
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.readFloat();
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
        }
    }
