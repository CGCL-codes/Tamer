    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing. IOException can " + "not be checked since is never thrown (primitive data " + "is written into a self-expanding buffer).", method = "writeShort", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readShort", args = {  }, clazz = ObjectInputStream.class) })
    public void test_read_writeShort() throws IOException {
        os.writeShort(9875);
        os.close();
        openObjectInputStream();
        assertEquals("Test 1: Incorrect short written or read;", 9875, is.readShort());
        try {
            is.readShort();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        is.close();
        try {
            is.readShort();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
