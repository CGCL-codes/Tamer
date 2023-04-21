    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "write", args = { int.class })
    public void test_writeI_MultiThread() throws IOException {
        final PipedReader pr = new PipedReader();
        final PipedWriter pw = new PipedWriter();
        pr.connect(pw);
        class WriteRunnable implements Runnable {

            boolean pass = false;

            boolean readerAlive = true;

            public void run() {
                try {
                    pw.write(1);
                    while (readerAlive) {
                        Thread.sleep(100);
                    }
                    try {
                        pw.write(1);
                    } catch (IOException e) {
                        pass = true;
                    }
                } catch (IOException e) {
                } catch (InterruptedException e) {
                }
            }
        }
        WriteRunnable writeRunnable = new WriteRunnable();
        Thread writeThread = new Thread(writeRunnable);
        class ReadRunnable implements Runnable {

            boolean pass;

            public void run() {
                try {
                    pr.read();
                    pass = true;
                } catch (IOException e) {
                }
            }
        }
        ReadRunnable readRunnable = new ReadRunnable();
        Thread readThread = new Thread(readRunnable);
        writeThread.start();
        readThread.start();
        while (readThread.isAlive()) {
        }
        writeRunnable.readerAlive = false;
        assertTrue("reader thread failed to read", readRunnable.pass);
        while (writeThread.isAlive()) {
        }
        assertTrue("writer thread failed to recognize dead reader", writeRunnable.pass);
    }
