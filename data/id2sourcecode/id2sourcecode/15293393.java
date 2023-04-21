    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeFloat", args = { float.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readFloat", args = {  }, clazz = DataInputStream.class) })
    public void test_read_writeFloat() throws IOException {
        os.writeFloat(29.08764f);
        sos.setThrowsException(true);
        try {
            os.writeFloat(29.08764f);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        sos.setThrowsException(false);
        os.close();
        openDataInputStream();
        assertEquals("Test 2: Incorrect float written or read;", 29.08764f, dis.readFloat());
        try {
            dis.readFloat();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
        }
        dis.close();
        try {
            dis.readFloat();
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
    }
