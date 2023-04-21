    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeByte", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readByte", args = {  }) })
    public void test_read_writeByte() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeByte(Byte.MIN_VALUE);
        raf.writeByte(11);
        raf.writeByte(Byte.MAX_VALUE);
        raf.writeByte(Byte.MIN_VALUE - 1);
        raf.writeByte(Byte.MAX_VALUE + 1);
        raf.seek(0);
        assertEquals("Test 1: Incorrect value written or read;", Byte.MIN_VALUE, raf.readByte());
        assertEquals("Test 2: Incorrect value written or read;", 11, raf.readByte());
        assertEquals("Test 3: Incorrect value written or read;", Byte.MAX_VALUE, raf.readByte());
        assertEquals("Test 4: Incorrect value written or read;", 127, raf.readByte());
        assertEquals("Test 5: Incorrect value written or read;", -128, raf.readByte());
        try {
            raf.readByte();
            fail("Test 6: EOFException expected.");
        } catch (EOFException e) {
        }
        raf.close();
        try {
            raf.writeByte(13);
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.readByte();
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
        }
    }
