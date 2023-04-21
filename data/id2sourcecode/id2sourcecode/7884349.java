    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeLong", args = { long.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readLong", args = {  }) })
    public void test_read_writeLong() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeLong(Long.MIN_VALUE);
        raf.writeLong('T');
        raf.writeLong(Long.MAX_VALUE);
        raf.writeLong(Long.MIN_VALUE - 1);
        raf.writeLong(Long.MAX_VALUE + 1);
        raf.seek(0);
        assertEquals("Test 1: Incorrect value written or read;", Long.MIN_VALUE, raf.readLong());
        assertEquals("Test 2: Incorrect value written or read;", 'T', raf.readLong());
        assertEquals("Test 3: Incorrect value written or read;", Long.MAX_VALUE, raf.readLong());
        assertEquals("Test 4: Incorrect value written or read;", 0x7fffffffffffffffl, raf.readLong());
        assertEquals("Test 5: Incorrect value written or read;", 0x8000000000000000l, raf.readLong());
        try {
            raf.readLong();
            fail("Test 6: EOFException expected.");
        } catch (EOFException e) {
        }
        raf.close();
        try {
            raf.writeLong('E');
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.readLong();
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
        }
    }
