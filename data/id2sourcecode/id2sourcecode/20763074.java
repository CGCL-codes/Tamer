    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Checks that read() returns -1 if the PipedWriter connectedto this PipedReader is closed.", method = "read", args = {  })
    public void test_read$CII_2() throws Exception {
        Thread writerThread;
        PipedWriter pw;
        PWriter2 pwriter;
        char[] c = new char[1];
        preader = new PipedReader();
        pw = new PipedWriter(preader);
        writerThread = new Thread(pwriter = new PWriter2(pw), "PWriter2");
        writerThread.start();
        Thread.sleep(500);
        preader.read(c, 0, 1);
        assertEquals("Test 1: No more data indication expected. ", -1, preader.read(c, 0, 1));
        pwriter.keepRunning = false;
    }
