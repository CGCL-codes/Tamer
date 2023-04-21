    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing. IOException can " + "not be checked since is never thrown (primitive data " + "is written into a self-expanding buffer).", method = "writeDouble", args = { double.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readDouble", args = {  }, clazz = ObjectInputStream.class) })
    public void test_read_writeDouble() throws IOException {
        os.writeDouble(2345.76834720202);
        os.close();
        openObjectInputStream();
        assertEquals("Test 1: Incorrect double written or read;", 2345.76834720202, is.readDouble());
        try {
            is.readDouble();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        is.close();
        try {
            is.readDouble();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
