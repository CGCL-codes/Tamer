    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeByte", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readByte", args = {  }, clazz = DataInputStream.class) })
    public void test_read_writeByte() throws IOException {
        os.writeByte((byte) 127);
        sos.setThrowsException(true);
        try {
            os.writeByte((byte) 127);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        sos.setThrowsException(false);
        os.close();
        openDataInputStream();
        assertEquals("Test 2: Incorrect byte written or read;", (byte) 127, dis.readByte());
        try {
            dis.readByte();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
        }
        dis.close();
        try {
            dis.readByte();
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
    }
