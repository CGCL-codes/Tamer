    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "receive", args = { int.class })
    public void test_receive() throws IOException {
        pis = new PipedInputStream();
        pos = new PipedOutputStream();
        pis.connect(pos);
        class WriteRunnable implements Runnable {

            boolean pass = false;

            boolean readerAlive = true;

            public void run() {
                try {
                    pos.write(1);
                    while (readerAlive) {
                        Thread.sleep(100);
                    }
                    try {
                        pos.write(new byte[BUFFER_SIZE]);
                        pos.write(1);
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
                    pis.read();
                    pass = true;
                } catch (IOException e) {
                }
            }
        }
        ;
        ReadRunnable readRunnable = new ReadRunnable();
        Thread readThread = new Thread(readRunnable);
        writeThread.start();
        readThread.start();
        while (readThread.isAlive()) ;
        writeRunnable.readerAlive = false;
        assertTrue("reader thread failed to read", readRunnable.pass);
        while (writeThread.isAlive()) ;
        assertTrue("writer thread failed to recognize dead reader", writeRunnable.pass);
        pis = new PipedInputStream();
        pos = new PipedOutputStream();
        pis.connect(pos);
        class MyRunnable implements Runnable {

            boolean pass;

            public void run() {
                try {
                    pos.write(1);
                } catch (IOException e) {
                    pass = true;
                }
            }
        }
        MyRunnable myRun = new MyRunnable();
        synchronized (pis) {
            t = new Thread(myRun);
            t.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            pos.close();
        }
        while (t.isAlive()) {
            ;
        }
        assertTrue("write failed to throw IOException on closed PipedOutputStream", myRun.pass);
    }
