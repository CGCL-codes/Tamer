    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeChar", args = { int.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readChar", args = {  }, clazz = DataInputStream.class) })
    public void test_read_writeChar() throws IOException {
        os.writeChar('b');
        sos.setThrowsException(true);
        try {
            os.writeChar('k');
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        sos.setThrowsException(false);
        os.close();
        openDataInputStream();
        assertEquals("Test 2: Incorrect char written or read;", 'b', dis.readChar());
        try {
            dis.readChar();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
        }
        dis.close();
        try {
            dis.readChar();
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
    }
