    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "write", args = { char[].class, int.class, int.class })
    public void test_write$CII() throws Exception {
        pw = new PipedWriter();
        try {
            pw.write(testBuf, 0, 5);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        pw = new PipedWriter(new PipedReader());
        try {
            pw.write(testBuf, -1, 1);
            fail("Test 2: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            pw.write(testBuf, 0, -1);
            fail("Test 3: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            pw.write(testBuf, 5, testString.length());
            fail("Test 4: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
        }
        pw.close();
        pw = new PipedWriter();
        try {
            readerThread = new Thread(reader = new PReader(pw), "writeCII");
            readerThread.start();
            pw.write(testBuf, 0, testLength);
            pw.close();
            reader.read(testLength);
            assertTrue("Test 5: Characters read do not match the characters written.", Arrays.equals(testBuf, reader.buf));
        } catch (IOException e) {
            fail("Test 5: Unexpected IOException: " + e.getMessage());
        }
        readerThread.interrupt();
        try {
            pw.write(testBuf, 0, 5);
            fail("Test 6: IOException expected.");
        } catch (IOException e) {
        }
        reader.pr.close();
        try {
            pw.write(testBuf, 0, 5);
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
        }
    }
