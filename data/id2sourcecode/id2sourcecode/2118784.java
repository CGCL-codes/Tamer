    public void transmit() {
        if (writeBuffer.limit() == buffSize) writeBuffer.flip();
        if (readBuffer.limit() != buffSize) readBuffer.compact();
        readBuffer.put(writeBuffer);
        writeBuffer.clear();
        readBuffer.flip();
    }
