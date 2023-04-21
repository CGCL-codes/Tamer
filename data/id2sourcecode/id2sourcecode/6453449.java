    private void verifyWriteRead(int writeSize, int expectedWritten, int readSize, int expectedReadSize) throws IOException {
        byte data[] = verifyWrite(writeSize, expectedWritten);
        verifyRead(readSize, expectedReadSize, data, 0);
        assertEquals("available", 0, is.available());
    }
