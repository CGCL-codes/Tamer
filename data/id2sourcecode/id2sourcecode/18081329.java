    private void verifyWriteRead(int writeSize, int expectedWritten, int readSize, int expectedReadSize) {
        byte data[] = verifyWrite(writeSize, expectedWritten);
        verifyRead(readSize, expectedReadSize, data, 0);
        assertEquals("available", 0, NativeTestInterfaces.testReceiveBufferAvailable(bufferHandler));
        assertEquals("read not available", -1, NativeTestInterfaces.testReceiveBufferRead(bufferHandler));
    }
