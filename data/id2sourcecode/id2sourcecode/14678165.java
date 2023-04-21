    @Override
    public boolean writeRead(int address, boolean tenBitAddr, byte[] writeData, int writeSize, byte[] readData, int readSize) throws ConnectionLostException, InterruptedException {
        Result result = writeReadAsync(address, tenBitAddr, writeData, writeSize, readData, readSize);
        return result.waitReady();
    }
