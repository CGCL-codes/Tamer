    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeUTF", args = { String.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readUTF", args = {  }, clazz = DataInputStream.class) })
    public void test_read_writeUTF() throws IOException {
        os.writeUTF(unihw);
        sos.setThrowsException(true);
        try {
            os.writeUTF(unihw);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        sos.setThrowsException(false);
        os.close();
        openDataInputStream();
        assertTrue("Test 1: Incorrect UTF-8 string written or read.", dis.readUTF().equals(unihw));
        try {
            dis.readUTF();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        dis.close();
        try {
            dis.readUTF();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
