    @MediumTest
    public void testA() throws Exception {
        final PipedInputStream in = new PipedInputStream();
        final PipedOutputStream out = new PipedOutputStream(in);
        assertEquals(0, in.available());
        TestThread reader, writer;
        reader = new TestThread() {

            Fibonacci fib = new Fibonacci();

            @Override
            public void runTest() throws Exception {
                int readInt;
                byte readByte;
                for (; ; ) {
                    readInt = in.read();
                    if (readInt == -1) {
                        return;
                    }
                    readByte = (byte) readInt;
                    assertEquals(readByte, (byte) fib.next());
                    countRead++;
                }
            }
        };
        reader.start();
        writer = new TestThread() {

            Fibonacci fib = new Fibonacci();

            @Override
            public void runTest() throws Exception {
                for (int i = 0; i < 2000; i++) {
                    int toWrite = fib.next();
                    out.write(toWrite);
                }
                out.close();
            }
        };
        writer.start();
        for (; ; ) {
            try {
                reader.join(60 * 1000);
                writer.join(1000);
                break;
            } catch (InterruptedException ex) {
            }
        }
        assertEquals(2000, reader.countRead);
        if (writer.exception != null) {
            throw new Exception(writer.exception);
        }
        if (reader.exception != null) {
            throw new Exception(reader.exception);
        }
    }
