    public void test_read_LByteBuffer_NonBlocking_ReadWriteRealLargeData() throws Exception {
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(localAddr1);
        ByteBuffer buf = ByteBuffer.allocate(CAPACITY_64KB);
        for (int i = 0; i < CAPACITY_64KB; i++) {
            buf.put((byte) i);
        }
        buf.flip();
        clientChannel.connect(localAddr1);
        WriteChannelThread writeThread = new WriteChannelThread(clientChannel, buf);
        writeThread.start();
        Socket socket = serverChannel.accept().socket();
        InputStream in = socket.getInputStream();
        assertReadResult(in, CAPACITY_64KB);
        writeThread.join();
        if (writeThread.exception != null) {
            throw writeThread.exception;
        }
    }
