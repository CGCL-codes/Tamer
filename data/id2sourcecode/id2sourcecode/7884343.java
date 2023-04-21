    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeBoolean", args = { boolean.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readBoolean", args = {  }) })
    public void test_read_writeBoolean() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeBoolean(true);
        raf.writeBoolean(false);
        raf.seek(0);
        assertEquals("Test 1: Incorrect value written or read;", true, raf.readBoolean());
        assertEquals("Test 2: Incorrect value written or read;", false, raf.readBoolean());
        try {
            raf.readBoolean();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
        }
        raf.close();
        try {
            raf.writeBoolean(false);
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.readBoolean();
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
        }
    }
