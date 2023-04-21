    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeInt", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readInt", args = {  }) })
    public void test_read_writeInt() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeInt(Integer.MIN_VALUE);
        raf.writeInt('T');
        raf.writeInt(Integer.MAX_VALUE);
        raf.writeInt(Integer.MIN_VALUE - 1);
        raf.writeInt(Integer.MAX_VALUE + 1);
        raf.seek(0);
        assertEquals("Test 1: Incorrect value written or read;", Integer.MIN_VALUE, raf.readInt());
        assertEquals("Test 2: Incorrect value written or read;", 'T', raf.readInt());
        assertEquals("Test 3: Incorrect value written or read;", Integer.MAX_VALUE, raf.readInt());
        assertEquals("Test 4: Incorrect value written or read;", 0x7fffffff, raf.readInt());
        assertEquals("Test 5: Incorrect value written or read;", 0x80000000, raf.readInt());
        try {
            raf.readInt();
            fail("Test 6: EOFException expected.");
        } catch (EOFException e) {
        }
        raf.close();
        try {
            raf.writeInt('E');
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.readInt();
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
        }
    }
