    public void test_write_$CII_MultiThread() throws Exception {
        final PipedReader pr = new PipedReader();
        final PipedWriter pw = new PipedWriter();
        pr.connect(pw);
        class WriteRunnable implements Runnable {

            boolean pass = false;

            volatile boolean readerAlive = true;

            public void run() {
                try {
                    pw.write(1);
                    while (readerAlive) {
                    }
                    try {
                        char[] buf = new char[10];
                        pw.write(buf, 0, 10);
                    } catch (IOException e) {
                        pass = true;
                    }
                } catch (IOException e) {
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
