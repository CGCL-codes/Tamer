    public void test_write_LByteBuffer_mutliThread() throws IOException, InterruptedException {
        final int THREAD_NUM = 20;
        final byte[] strbytes = "bytes".getBytes(ISO8859_1);
        Thread[] thread = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            thread[i] = new Thread() {

                public void run() {
                    try {
                        sink.write(ByteBuffer.wrap(strbytes));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            thread[i].start();
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            thread[i].join();
        }
        ByteBuffer readBuf = ByteBuffer.allocate(THREAD_NUM * BUFFER_SIZE);
        long totalCount = 0;
        do {
            long count = source.read(readBuf);
            if (count < 0) {
                break;
            }
            totalCount += count;
        } while (totalCount != (THREAD_NUM * BUFFER_SIZE));
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < THREAD_NUM; i++) {
            buf.append("bytes");
        }
        String readString = buf.toString();
        assertEquals(readString, new String(readBuf.array(), ISO8859_1));
    }
