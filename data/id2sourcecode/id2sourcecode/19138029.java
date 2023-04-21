    @Override
    public int getRowCount() {
        int readLength = Math.max(0, computeReadLength());
        int writeLength = Math.max(0, computeWriteLength());
        return Math.max(readLength, writeLength);
    }
