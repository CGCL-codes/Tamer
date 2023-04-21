    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing. IOException can " + "not be checked since is never thrown (primitive data " + "is written into a self-expanding buffer).", method = "writeLong", args = { long.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readLong", args = {  }, clazz = ObjectInputStream.class) })
    public void test_read_writeLong() throws IOException {
        os.writeLong(9875645283333L);
        os.close();
        openObjectInputStream();
        assertEquals("Test 1: Incorrect long written or read;", 9875645283333L, is.readLong());
        try {
            is.readLong();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        is.close();
        try {
            is.readLong();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
