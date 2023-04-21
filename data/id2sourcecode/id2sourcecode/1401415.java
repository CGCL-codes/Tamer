    @MediumTest
    public void testB() throws Exception {
        final PipedInputStream in = new PipedInputStream();
        final PipedOutputStream out = new PipedOutputStream(in);
        assertEquals(0, in.available());
        TestThread reader, writer;
        reader = new TestThread() {

            Fibonacci fib = new Fibonacci();

            @Override
            public void runTest() throws Exception {
                byte readBytes[] = new byte[5];
                int ret;
                for (; ; ) {
                    int nread = 0;
                    while (nread < 5) {
                        ret = in.read(readBytes, nread, readBytes.length - nread);
                        if (ret == -1) {
                            return;
                        }
                        nread += ret;
                    }
                    assertEquals(5, nread);
                    int readInt = (((int) readBytes[0] & 0xff) << 24) | (((int) readBytes[1] & 0xff) << 16) | (((int) readBytes[2] & 0xff) << 8) | (((int) readBytes[3] & 0xff));
                    assertEquals("Error at " + countRead, fib.next(), readInt);
                    assertEquals("Error at " + countRead, 0, readBytes[4]);
                    countRead++;
                }
            }
        };
        reader.start();
        writer = new TestThread() {

            Fibonacci fib = new Fibonacci();

            @Override
            public void runTest() throws Exception {
                byte writeBytes[] = new byte[5];
                for (int i = 0; i < 2000; i++) {
                    int toWrite = fib.next();
                    writeBytes[0] = (byte) (toWrite >> 24);
                    writeBytes[1] = (byte) (toWrite >> 16);
                    writeBytes[2] = (byte) (toWrite >> 8);
                    writeBytes[3] = (byte) (toWrite);
                    writeBytes[4] = 0;
                    out.write(writeBytes, 0, writeBytes.length);
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
        if (reader.exception != null) {
            throw new Exception(reader.exception);
        }
        if (writer.exception != null) {
            throw new Exception(writer.exception);
        }
        assertEquals(2000, reader.countRead);
    }
