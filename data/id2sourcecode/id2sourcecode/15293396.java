    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeShort", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readShort", args = {  }, clazz = DataInputStream.class) })
    public void test_read_writeShort() throws IOException {
        os.writeShort(9875);
        sos.setThrowsException(true);
        try {
            os.writeShort(9875);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        sos.setThrowsException(false);
        os.close();
        openDataInputStream();
        assertEquals("Test 1: Incorrect short written or read;", 9875, dis.readShort());
        try {
            dis.readShort();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        dis.close();
        try {
            dis.readShort();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
