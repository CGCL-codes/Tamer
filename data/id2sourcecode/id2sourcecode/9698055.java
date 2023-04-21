    @Override
    public void writeRead(int slave, byte[] writeData, int writeSize, int totalSize, byte[] readData, int readSize) throws ConnectionLostException, InterruptedException {
        Result result = writeReadAsync(slave, writeData, writeSize, totalSize, readData, readSize);
        result.waitReady();
    }
