    public synchronized void i2cWriteRead(int i2cNum, boolean tenBitAddr, int address, int writeSize, int readSize, byte[] writeData) throws IOException {
        writeByte(I2C_WRITE_READ);
        writeByte(((address >> 8) << 6) | (tenBitAddr ? 0x20 : 0x00) | i2cNum);
        writeByte(address & 0xFF);
        writeByte(writeSize);
        writeByte(readSize);
        for (int i = 0; i < writeSize; ++i) {
            writeByte(((int) writeData[i]) & 0xFF);
        }
        flush();
    }
