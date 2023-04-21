    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeUTF", args = { java.lang.String.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readUTF", args = {  }) })
    public void test_read_writeUTF() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeUTF(unihw);
        raf.seek(0);
        assertEquals("Test 1: Incorrect UTF string written or read;", unihw, raf.readUTF());
        try {
            raf.readUTF();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        raf.close();
        try {
            raf.writeUTF("Already closed.");
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.readUTF();
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
    }
