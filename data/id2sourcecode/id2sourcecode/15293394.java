    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeInt", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readInt", args = {  }, clazz = DataInputStream.class) })
    public void test_read_writeInt() throws IOException {
        os.writeInt(768347202);
        sos.setThrowsException(true);
        try {
            os.writeInt(768347202);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        sos.setThrowsException(false);
        os.close();
        openDataInputStream();
        assertEquals("Test 1: Incorrect int written or read;", 768347202, dis.readInt());
        try {
            dis.readInt();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        dis.close();
        try {
            dis.readInt();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
