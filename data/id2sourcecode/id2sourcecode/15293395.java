    @TestTargets({ @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeLong", args = { long.class }), @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "readLong", args = {  }, clazz = DataInputStream.class) })
    public void test_read_writeLong() throws IOException {
        os.writeLong(9875645283333L);
        sos.setThrowsException(true);
        try {
            os.writeLong(9875645283333L);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        sos.setThrowsException(false);
        os.close();
        openDataInputStream();
        assertEquals("Test 2: Incorrect long written or read;", 9875645283333L, dis.readLong());
        try {
            dis.readLong();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
        }
        dis.close();
        try {
            dis.readLong();
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
        }
    }
