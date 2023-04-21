    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing. IOException can " + "not be checked since is never thrown (primitive data " + "is written into a self-expanding buffer).", method = "writeInt", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readInt", args = {  }, clazz = ObjectInputStream.class) })
    public void test_read_writeInt() throws IOException {
        os.writeInt(768347202);
        os.close();
        openObjectInputStream();
        assertEquals("Test 1: Incorrect int written or read;", 768347202, is.readInt());
        try {
            is.readInt();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        is.close();
        try {
            is.readInt();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
