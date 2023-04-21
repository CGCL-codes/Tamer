    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeShort", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readShort", args = {  }) })
    public void test_read_writeShort() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeShort(Short.MIN_VALUE);
        raf.writeShort('T');
        raf.writeShort(Short.MAX_VALUE);
        raf.writeShort(Short.MIN_VALUE - 1);
        raf.writeShort(Short.MAX_VALUE + 1);
        raf.seek(0);
        assertEquals("Test 1: Incorrect value written or read;", Short.MIN_VALUE, raf.readShort());
        assertEquals("Test 2: Incorrect value written or read;", 'T', raf.readShort());
        assertEquals("Test 3: Incorrect value written or read;", Short.MAX_VALUE, raf.readShort());
        assertEquals("Test 4: Incorrect value written or read;", 0x7fff, raf.readShort());
        assertEquals("Test 5: Incorrect value written or read;", (short) 0x8000, raf.readShort());
        try {
            raf.readShort();
            fail("Test 6: EOFException expected.");
        } catch (EOFException e) {
        }
        raf.close();
        try {
            raf.writeShort('E');
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.readShort();
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
        }
    }
