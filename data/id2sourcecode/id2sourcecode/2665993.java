    public void test_write_LByteBuffer_Blocking_ReadWriteRealLargeData() throws Exception {
        serverChannel.socket().bind(localAddr1);
        byte[] writeContent = new byte[CAPACITY_64KB];
        for (int i = 0; i < writeContent.length; i++) {
            writeContent[i] = (byte) i;
        }
        clientChannel.connect(localAddr1);
        Socket socket = serverChannel.accept().socket();
        WriteSocketThread writeThread = new WriteSocketThread(socket, writeContent);
        writeThread.start();
        assertWriteResult(CAPACITY_64KB);
        writeThread.join();
        if (writeThread.exception != null) {
            throw writeThread.exception;
        }
    }
