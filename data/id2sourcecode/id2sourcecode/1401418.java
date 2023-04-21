    @SmallTest
    public void testC() throws Exception {
        final PipedInputStream in = new PipedInputStream();
        final PipedOutputStream out = new PipedOutputStream(in);
        final byte readBytes[] = new byte[1024 * 2];
        assertEquals(0, in.available());
        TestThread reader, writer;
        reader = new TestThread() {

            @Override
            public void runTest() throws Exception {
                int ret;
                for (; ; ) {
                    int nread = 0;
                    while (nread < readBytes.length) {
                        ret = in.read(readBytes, nread, readBytes.length - nread);
                        if (ret == -1) {
                            return;
                        }
                        nread += ret;
                    }
                }
            }
        };
        reader.start();
        writer = new TestThread() {

            Fibonacci fib = new Fibonacci();

            @Override
            public void runTest() throws Exception {
                byte writeBytes[] = new byte[1024 * 2];
                for (int i = 0; i < (writeBytes.length - 4); i += 4) {
                    int toWrite = fib.next();
                    writeBytes[i] = (byte) (toWrite >> 24);
                    writeBytes[i + 1] = (byte) (toWrite >> 16);
                    writeBytes[i + 2] = (byte) (toWrite >> 8);
                    writeBytes[i + 3] = (byte) (toWrite);
                }
                out.write(writeBytes, 0, writeBytes.length);
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
        Fibonacci fib = new Fibonacci();
        for (int i = 0; i < (readBytes.length - 4); i += 4) {
            int readInt = (((int) readBytes[i] & 0xff) << 24) | (((int) readBytes[i + 1] & 0xff) << 16) | (((int) readBytes[i + 2] & 0xff) << 8) | (((int) readBytes[i + 3] & 0xff));
            assertEquals("Error at " + i, readInt, fib.next());
        }
    }
