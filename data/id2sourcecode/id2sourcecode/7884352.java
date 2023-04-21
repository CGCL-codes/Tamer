    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeBytes", args = { java.lang.String.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readFully", args = { byte[].class }) })
    public void test_readFully$B_writeBytesLjava_lang_String() throws IOException {
        byte[] buf = new byte[testLength];
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeBytes(testString);
        raf.seek(0);
        try {
            raf.readFully(null);
            fail("Test 1: NullPointerException expected.");
        } catch (NullPointerException e) {
        }
        raf.readFully(buf);
        assertEquals("Test 2: Incorrect bytes written or read;", testString, new String(buf));
        try {
            raf.readFully(buf);
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
        }
        raf.close();
        try {
            raf.writeBytes("Already closed.");
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
        try {
            raf.readFully(buf);
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
        }
    }
