    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeDouble", args = { double.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readDouble", args = {  }, clazz = DataInputStream.class) })
    public void test_read_writeDouble() throws IOException {
        os.writeDouble(2345.76834720202);
        sos.setThrowsException(true);
        try {
            os.writeDouble(2345.76834720202);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        sos.setThrowsException(false);
        os.close();
        openDataInputStream();
        assertEquals("Test 1: Incorrect double written or read;", 2345.76834720202, dis.readDouble());
        try {
            dis.readDouble();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
        }
        dis.close();
        try {
            dis.readDouble();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
        }
    }
