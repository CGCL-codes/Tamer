    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "connect", args = { java.io.PipedWriter.class })
    public void test_connectLjava_io_PipedWriter() throws Exception {
        char[] c = null;
        preader = new PipedReader();
        t = new Thread(pwriter = new PWriter(), "");
        preader.connect(pwriter.pw);
        t.start();
        Thread.sleep(500);
        c = new char[11];
        preader.read(c, 0, 11);
        assertEquals("Test 1: Wrong characters read. ", "Hello World", new String(c));
        try {
            preader.connect(new PipedWriter());
            fail("Test 2: IOException expected because the reader is already connected.");
        } catch (IOException e) {
        }
    }
