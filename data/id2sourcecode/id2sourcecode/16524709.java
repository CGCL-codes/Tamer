    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing. IOException can " + "not be checked since is never thrown (primitive data " + "is written into a self-expanding buffer).", method = "writeUTF", args = { String.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readUTF", args = {  }, clazz = ObjectInputStream.class) })
    public void test_read_writeUTF() throws IOException {
        os.writeUTF(unihw);
        os.close();
        openObjectInputStream();
        assertTrue("Test 1: Incorrect UTF-8 string written or read.", is.readUTF().equals(unihw));
        try {
            is.readUTF();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        is.close();
        try {
            is.readUTF();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
