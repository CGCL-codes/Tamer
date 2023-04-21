    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing. IOException can " + "not be checked since is never thrown (primitive data " + "is written into a self-expanding buffer).", method = "writeByte", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readByte", args = {  }, clazz = ObjectInputStream.class) })
    public void test_read_writeByte() throws IOException {
        os.writeByte((byte) 127);
        os.close();
        openObjectInputStream();
        assertEquals("Test 1: Incorrect byte written or read;", (byte) 127, is.readByte());
        try {
            is.readByte();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        is.close();
        try {
            is.readByte();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
