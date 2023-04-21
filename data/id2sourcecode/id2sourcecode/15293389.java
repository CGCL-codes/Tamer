    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeBoolean", args = { boolean.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readBoolean", args = {  }, clazz = DataInputStream.class) })
    public void test_read_writeBoolean() throws IOException {
        os.writeBoolean(true);
        sos.setThrowsException(true);
        try {
            os.writeBoolean(false);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        sos.setThrowsException(false);
        os.close();
        openDataInputStream();
        assertTrue("Test 2: Incorrect boolean written or read.", dis.readBoolean());
        try {
            dis.readBoolean();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
        }
        dis.close();
        try {
            dis.readBoolean();
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
    }
