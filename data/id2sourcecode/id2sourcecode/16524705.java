    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing. IOException can " + "not be checked since is never thrown (primitive data " + "is written into a self-expanding buffer).", method = "writeFloat", args = { float.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readFloat", args = {  }, clazz = ObjectInputStream.class) })
    public void test_read_writeFloat() throws IOException {
        os.writeFloat(29.08764f);
        os.close();
        openObjectInputStream();
        assertEquals("Test 1: Incorrect float written or read;", 29.08764f, is.readFloat());
        try {
            is.readFloat();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        is.close();
        try {
            is.readFloat();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
