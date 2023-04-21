    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing. IOException can " + "not be checked since is never thrown (primitive data " + "is written into a self-expanding buffer).", method = "writeChar", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readChar", args = {  }, clazz = ObjectInputStream.class) })
    public void test_read_writeChar() throws IOException {
        os.writeChar('b');
        os.close();
        openObjectInputStream();
        assertEquals("Test 1: Incorrect char written or read;", 'b', is.readChar());
        try {
            is.readChar();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        is.close();
        try {
            is.readChar();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
