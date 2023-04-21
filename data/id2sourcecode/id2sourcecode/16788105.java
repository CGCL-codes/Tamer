    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Checks that read returns -1 if the PipedOutputStream connected to this PipedInputStream is closed.", method = "read", args = {  })
    public void test_read_2() throws Exception {
        Thread writerThread;
        PWriter2 pwriter;
        pos = new PipedOutputStream();
        pis = new PipedInputStream(pos);
        writerThread = new Thread(pwriter = new PWriter2(pos));
        writerThread.start();
        synchronized (pwriter) {
            pwriter.wait(5000);
        }
        pis.read();
        assertEquals("Test 1: No more data indication expected. ", -1, pis.read());
        pwriter.keepRunning = false;
        pis.close();
    }
