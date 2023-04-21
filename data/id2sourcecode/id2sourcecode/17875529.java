    public int readWrite(byte[] dstBuffer, int offset, int length) {
        int bytesReadWritten = 0;
        while (this.readPosition != this.writePosition && bytesReadWritten < length) {
            dstBuffer[offset + bytesReadWritten] = this.buffer[this.readPosition];
            this.buffer[this.writePosition] = dstBuffer[offset + bytesReadWritten];
            bytesReadWritten++;
            this.readPosition++;
            this.writePosition++;
            this.readPosition = this.readPosition % this.buffer.length;
            this.writePosition = this.writePosition % this.buffer.length;
        }
        return bytesReadWritten;
    }
