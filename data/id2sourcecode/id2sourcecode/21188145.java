    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "write", args = { int.class })
    public void test_writeI() throws Exception {
        pw = new PipedWriter();
        try {
            pw.write(42);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
        }
        readerThread = new Thread(reader = new PReader(pw), "writeI");
        readerThread.start();
        pw.write(1);
        pw.write(2);
        pw.write(3);
        pw.close();
        reader.read(3);
        assertTrue("Test 2: The charaacters read do not match the characters written: " + (int) reader.buf[0] + " " + (int) reader.buf[1] + " " + (int) reader.buf[2], reader.buf[0] == 1 && reader.buf[1] == 2 && reader.buf[2] == 3);
    }
