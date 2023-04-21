    @Override
    public void writeRead(byte[] writeData, int writeSize, int totalSize, byte[] readData, int readSize) throws ConnectionLostException, InterruptedException {
        writeRead(0, writeData, writeSize, totalSize, readData, readSize);
    }
