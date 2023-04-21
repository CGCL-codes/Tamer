    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeChar", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readChar", args = {  }) })
    public void test_read_writeChar() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeChar(Character.MIN_VALUE);
        raf.writeChar('T');
        raf.writeChar(Character.MAX_VALUE);
        raf.writeChar(Character.MIN_VALUE - 1);
        raf.writeChar(Character.MAX_VALUE + 1);
        raf.seek(0);
        assertEquals("Test 1: Incorrect value written or read;", Character.MIN_VALUE, raf.readChar());
        assertEquals("Test 2: Incorrect value written or read;", 'T', raf.readChar());
        assertEquals("Test 3: Incorrect value written or read;", Character.MAX_VALUE, raf.readChar());
        assertEquals("Test 4: Incorrect value written or read;", 0xffff, raf.readChar());
        assertEquals("Test 5: Incorrect value written or read;", 0, raf.readChar());
        try {
            raf.readChar();
            fail("Test 6: EOFException expected.");
        } catch (EOFException e) {
        }
        raf.close();
        try {
            raf.writeChar('E');
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.readChar();
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
        }
    }
